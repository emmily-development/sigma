package dev.emmily.sigma.api.service;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;

public interface CachedModelService<T extends Model>
  extends ModelService<T> {
  void cache(T model);

  T findCached(String id);

  default T getOrFindCached(String id) {
    T model = findCached(id);

    if (model == null) {
      model = find(id);
    }

    return model;
  }

  T findByQueryCached(Object query);

  default T getOrFindByQueryCached(Object query) {
    T model = findByQueryCached(query);

    if (model == null) {
      model = findByQuery(query);
    }

    return model;
  }

  List<T> findManyCached(
    List<String> ids,
    int limit
  );

  default List<T> findManyCached(List<String> ids) {
    return findManyCached(ids, -1);
  }

  default List<T> findManyCached(
    int limit,
    String... ids
  ) {
    return findManyCached(Arrays.asList(ids), limit);
  }

  default List<T> findManyCached(String... ids) {
    return findManyCached(-1, ids);
  }

  List<T> findManyByQueryCached(
    Object query,
    int limit
  );

  default List<T> findManyByQueryCached(Object query) {
    return findManyByQueryCached(query, -1);
  }

  List<T> findAllCached();

  void deleteCached(String id);

  default void deleteCached(T model) {
    deleteCached(model.getId());
  }

  void deleteByQueryCached(Object query);

  void deleteManyCached(List<String> ids);

  default void deleteManyCached(String... ids) {
    deleteManyCached(Arrays.asList(ids));
  }

  void deleteManyByQueryCached(
    Object query,
    int limit
  );

  default void deleteManyByQueryCached(Object query) {
    deleteManyByQueryCached(query, -1);
  }
}
