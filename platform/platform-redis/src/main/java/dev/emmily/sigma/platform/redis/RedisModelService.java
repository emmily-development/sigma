package dev.emmily.sigma.platform.redis;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.codec.ModelCodec;
import dev.emmily.sigma.api.service.ModelService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;
import team.unnamed.reflect.identity.TypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedisModelService<T extends Model>
  implements ModelService<T> {
  private static final IllegalArgumentException INVALID_QUERY = new IllegalArgumentException(
    "RedisModelService only accepts queries of type String"
  );
  private final JedisPool jedisPool;
  private final ModelCodec modelCodec;
  private final String namespace;
  private final TypeReference<T> type;
  private final String typeNamespace;
  private final int ttl;

  public RedisModelService(
    JedisPool jedisPool,
    ModelCodec modelCodec,
    String namespace,
    TypeReference<T> type,
    int ttl) {
    this.jedisPool = jedisPool;
    this.modelCodec = modelCodec;
    this.namespace = namespace;
    this.type = type;
    this.typeNamespace = type.getTypeName();
    this.ttl = ttl;
  }

  public RedisModelService(
    JedisPool jedisPool,
    ModelCodec modelCodec,
    String namespace,
    Class<T> type,
    int ttl
  ) {
    this.jedisPool = jedisPool;
    this.modelCodec = modelCodec;
    this.namespace = namespace;
    this.type = TypeReference.of(type);
    this.typeNamespace = type.getName();
    this.ttl = ttl;
  }

  @Override
  public void create(T model) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.set(
        formatModel(model),
        modelCodec.serializeAsString(model),
        SetParams.setParams().ex(ttl)
      );
    }
  }

  @Override
  public T find(String id) {
    try (Jedis jedis = jedisPool.getResource()) {
      String formattedId = formatId(id);
      
      if (!jedis.exists(formattedId)) {
        return null;  
      }
      
      return modelCodec.deserializeFromString(
        jedis.get(formattedId),
        type
      );
    }
  }

  @Override
  public T findByQuery(Object query) {
    if (query instanceof String) {
      return find((String) query);
    }
    
    throw INVALID_QUERY;
  }

  @Override
  public List<T> findMany(
    List<String> ids,
    int limit
  ) {
    try (Jedis jedis = jedisPool.getResource()) {
      List<T> models = new ArrayList<>();
      
      for (String id : ids) {
        if (limit-- == 0) {
          break;
        }
        
        String formattedId = formatId(id);

        if (!jedis.exists(formattedId)) {
          continue;
        }

        models.add(modelCodec.deserializeFromString(
          jedis.get(formattedId),
          type
        ));
      }
      
      return models;
    }
  }

  @Override
  public List<T> findManyByQuery(
    Object query,
    int limit
  ) {
    return Collections.singletonList(findByQuery(query));
  }

  @Override
  public List<T> findAll() {
    try (Jedis jedis = jedisPool.getResource()) {
      List<T> models = new ArrayList<>();
      
      for (String key : jedis.keys(formatId("*"))) {
        models.add(modelCodec.deserializeFromString(
          jedis.get(key),
          type
        ));
      }
      
      return models;
    }
  }

  @Override
  public void delete(String id) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.del(formatId(id));
    }
  }

  @Override
  public void deleteByQuery(Object query) {
    if (query instanceof String) {
      delete((String) query);
    }

    throw INVALID_QUERY;
  }

  @Override
  public void deleteMany(List<String> ids) {
    try (Jedis jedis = jedisPool.getResource()) {
      for (String id : ids) {
        jedis.del(formatId(id));
      }
    }
  }

  @Override
  public void deleteManyByQuery(
    Object query,
    int limit
  ) {
    deleteByQuery(query);
  }

  public String formatId(String id) {
    return namespace + ":" + typeNamespace + ":" + id;
  }

  public String formatModel(T model) {
    return formatId(model.getId());
  }
}
