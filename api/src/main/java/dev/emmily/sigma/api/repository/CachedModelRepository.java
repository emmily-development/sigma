package dev.emmily.sigma.api.repository;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a {@link ModelRepository} linked to
 * another {@link ModelRepository}, which are
 * interdependent.
 *
 * @param <T> The type of model held by this
 *           repository.
 */
public interface CachedModelRepository<T extends Model>
  extends ModelRepository<T> {
  void cache(T model);

  T get(String id);

  default T getOrFind(String id) {
    T model = get(id);

    if (model == null) {
      model = get(id);
    }

    return model;
  }

  T getByQuery(Object query);

  default T getOrFindByQuery(Object query) {
    T model = getByQuery(query);

    if (model == null) {
      model = findByQuery(query);
    }

    return model;
  }

  List<T> getMany(
    List<String> ids,
    int limit
  );

  default List<T> getMany(List<String> ids) {
    return getMany(ids, -1);
  }

  default List<T> getMany(
    int limit,
    String... ids
  ) {
    return getMany(Arrays.asList(ids), limit);
  }

  default List<T> getMany(String... ids) {
    return getMany(-1, ids);
  }

  List<T> getManyByQuery(
    Object query,
    int limit
  );

  default List<T> getManyByQuery(Object query) {
    return getManyByQuery(query, -1);
  }

  List<T> getAll();

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

  void setCacheRepository(ModelRepository<T> repository);
}
