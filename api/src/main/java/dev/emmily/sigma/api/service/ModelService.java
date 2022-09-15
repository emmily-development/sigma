package dev.emmily.sigma.api.service;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;

/**
 * ModelService is a Service Provider Interface
 * intended to handle CRUD operations for a specific
 * type of object {@link T}. It allows the usage of
 * abstract query types, for example, Bson objects
 * when using MongoDB.
 *
 * @param <T> The type of model held by this
 *           model service.
 */
public interface ModelService<T extends Model> {
  void create(T model);

  T find(String id);

  T findByQuery(Object query);

  List<T> findMany(
    List<String> ids,
    int limit
  );

  default List<T> findMany(List<String> ids) {
    return findMany(ids, -1);
  }

  default List<T> findMany(
    int limit,
    String... ids
  ) {
    return findMany(Arrays.asList(ids), limit);
  }

  default List<T> findMany(String... ids) {
    return findMany(-1, ids);
  }

  List<T> findManyByQuery(
    Object query,
    int limit
  );

  default List<T> findManyByQuery(Object query) {
    return findManyByQuery(query, -1);
  }

  List<T> findAll();

  void delete(String id);

  default void delete(T model) {
    delete(model.getId());
  }

  void deleteByQuery(Object query);

  void deleteMany(List<String> ids);

  default void deleteMany(String... ids) {
    deleteMany(Arrays.asList(ids));
  }

  void deleteManyByQuery(
    Object query,
    int limit
  );

  default void deleteManyByQuery(Object query) {
    deleteManyByQuery(query, -1);
  }
}
