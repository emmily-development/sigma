package dev.emmily.sigma.platform.jdk;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.repository.ModelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * This is the default implementation of
 * {@link ModelRepository}, which, as its name
 * says, uses a {@link Map} to hold models.
 * It also serves as the first example on how
 * to create an own implementation of
 * {@link ModelRepository}, as it shows a basic
 * implementation of queries using
 * {@link Predicate predicates}.
 *
 * @param <T> The type of model held by this
 *           model repository.
 */
public class MapModelRepository<T extends Model>
  implements ModelRepository<T> {
  private static final IllegalArgumentException INVALID_QUERY = new IllegalArgumentException(
    "MapModelService only accepts queries of type Predicate<T extends Model>"
  );
  private final Map<String, T> modelRegistry;

  public MapModelRepository(Map<String, T> modelRegistry) {
    this.modelRegistry = modelRegistry;
  }

  public MapModelRepository() {
    this(new ConcurrentHashMap<>());
  }

  @Override
  public void create(T model) {
    modelRegistry.put(model.getId(), model);
  }

  @Override
  public boolean exists(String id) {
    return modelRegistry.containsKey(id);
  }

  @Override
  public T find(String id) {
    return modelRegistry.get(id);
  }

  @Override
  public T findByQuery(Object query) {
    if (!(query instanceof Predicate)) {
      throw INVALID_QUERY;
    }

    @SuppressWarnings("unchecked")
    Predicate<T> modelQuery = (Predicate<T>) query;

    for (T model : modelRegistry.values()) {
      if (modelQuery.test(model)) {
        return model;
      }
    }
    
    return null;
  }

  @Override
  public List<T> findMany(
    List<String> ids, 
    int limit
  ) {
    List<T> models = new ArrayList<>();

    for (T model : modelRegistry.values()) {
      if (ids.contains(model.getId())) {
        if (limit-- == 0) {
          break;
        }

        models.add(model);
      }
    }

    return models;
  }

  @Override
  public List<T> findManyByQuery(
    Object query,
    int limit
  ) {
    if (!(query instanceof Predicate)) {
      throw INVALID_QUERY;
    }

    @SuppressWarnings("unchecked")
    Predicate<T> modelQuery = (Predicate<T>) query;

    List<T> models = new ArrayList<>();

    for (T model : modelRegistry.values()) {
      if (modelQuery.test(model)) {
        if (limit-- == 0) {
          break;
        }

        models.add(model);
      }
    }

    return models;
  }

  @Override
  public List<T> findAll() {
    return new ArrayList<>(modelRegistry.values());
  }

  @Override
  public void delete(String id) {
    modelRegistry.remove(id);
  }

  @Override
  public void deleteByQuery(Object query) {
    if (!(query instanceof Predicate)) {
      throw INVALID_QUERY;
    }

    @SuppressWarnings("unchecked")
    Predicate<T> modelQuery = (Predicate<T>) query;

    for (T model : modelRegistry.values()) {
      if (modelQuery.test(model)) {
        modelRegistry.remove(model.getId());
      }
    }
  }

  @Override
  public void deleteMany(List<String> ids) {
    for (String id : ids) {
      modelRegistry.remove(id);
    }
  }

  @Override
  public void deleteManyByQuery(
    Object query,
    int limit
  ) {
    if (!(query instanceof Predicate)) {
      throw INVALID_QUERY;
    }

    @SuppressWarnings("unchecked")
    Predicate<T> modelQuery = (Predicate<T>) query;

    for (T model : modelRegistry.values()) {
      if (modelQuery.test(model)) {
        if (limit-- == 0) {
          break;
        }

        delete(model);
      }
    }
  }
}
