package dev.emmily.sigma.api.repository;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public abstract class CachedAsyncModelRepository<T extends Model>
  extends AsyncModelRepository<T>
  implements ModelRepository<T>, CachedModelRepository<T> {
  private ModelRepository<T> cacheModelRepository;

  public CachedAsyncModelRepository(
    Executor executor,
    ModelRepository<T> cacheModelRepository
  ) {
    super(executor);
    this.cacheModelRepository = cacheModelRepository;
  }

  public CachedAsyncModelRepository(Executor executor) {
    this(executor, null);
  }

  public CachedAsyncModelRepository(ModelRepository<T> cacheModelRepository) {
    this.cacheModelRepository = cacheModelRepository;
  }

  @Override
  public void cache(T model) {
    cacheModelRepository.create(model);
  }

  /**
   * @see #cache(Model)
   */
  public CompletableFuture<?> cacheAsync(T model) {
    return runAsync(() -> cache(model), executor);
  }

  @Override
  public T get(String id) {
    return cacheModelRepository.find(id);
  }

  /**
   * @see #get(String)
   */
  public CompletableFuture<T> getAsync(String id) {
    return supplyAsync(() -> this.get(id), executor);
  }

  /**
   * @see #getOrFind(String) (String)
   */
  public CompletableFuture<T> getOrFindAsync(String id) {
    return supplyAsync(() -> getOrFind(id), executor);
  }

  @Override
  public T getByQuery(Object query) {
    return cacheModelRepository.findByQuery(query);
  }

  /**
   * @see #getByQuery(Object)
   */
  public CompletableFuture<T> getByQueryAsync(Object query) {
    return supplyAsync(() -> getByQuery(query), executor);
  }

  /**
   * @see #getOrFindByQuery(Object)
   */
  public CompletableFuture<T> getOrFindByQueryAsync(Object query) {
    return supplyAsync(() -> getOrFindByQuery(query), executor);
  }

  @Override
  public List<T> getMany(
    List<String> ids,
    int limit
  ) {
    return cacheModelRepository.findMany(ids, limit);
  }

  /**
   * @see #getMany(List, int)
   */
  public CompletableFuture<List<T>> getManyAsync(
    List<String> ids,
    int limit
  ) {
    return supplyAsync(() -> getMany(ids, limit), executor);
  }

  /**
   * @see #getMany(List)
   */
  public CompletableFuture<List<T>> getManyAsync(List<String> ids) {
    return getManyAsync(ids, -1);
  }

  /**
   * @see #getMany(int, String...)
   */
  public CompletableFuture<List<T>> getManyAsync(
    int limit,
    String... ids
  ) {
    return getManyAsync(Arrays.asList(ids), limit);
  }

  /**
   * @see #getMany(String...)
   */
  public CompletableFuture<List<T>> getManyAsync(String... ids) {
    return getManyAsync(-1, ids);
  }

  @Override
  public List<T> getManyByQuery(
    Object query,
    int limit
  ) {
    return cacheModelRepository.findManyByQuery(query, limit);
  }

  /**
   * @see #getManyByQuery(Object, int)
   */
  public CompletableFuture<List<T>> getManyByQueryAsync(
    Object query,
    int limit
  ) {
    return supplyAsync(() -> getManyByQuery(query, limit));
  }

  /**
   * @see #getManyByQuery(Object)
   */
  public CompletableFuture<List<T>> getManyByQueryAsync(Object query) {
    return getManyByQueryAsync(query, -1);
  }

  @Override
  public List<T> getAll() {
    return cacheModelRepository.findAll();
  }

  /**
   * @see #getAll()
   */
  public CompletableFuture<List<T>> getAllAsync() {
    return supplyAsync(this::getAll, executor);
  }

  @Override
  public void deleteCached(String id) {
    T model = cacheModelRepository.find(id);

    if (model == null) {
      return;
    }

    cacheModelRepository.delete(id);
    create(model);
  }

  /**
   * @see #deleteCached(String)
   */
  public CompletableFuture<?> deleteCachedAsync(String id) {
    return runAsync(() -> deleteCached(id), executor);
  }

  /**
   * @see #deleteCachedAsync(Model)
   */
  public CompletableFuture<?> deleteCachedAsync(T model) {
    return deleteCachedAsync(model.getId());
  }

  @Override
  public void deleteByQueryCached(Object query) {
    T model = cacheModelRepository.findByQuery(query);

    if (model == null) {
      return;
    }

    cacheModelRepository.deleteByQuery(query);
    create(model);
  }

  /**
   * @see #deleteByQueryCached(Object)
   */
  public CompletableFuture<?> deleteByQueryCachedAsync(Object query) {
    return runAsync(() -> deleteByQueryCached(query), executor);
  }

  @Override
  public void deleteManyCached(List<String> ids) {
    List<T> models = cacheModelRepository.findMany(ids);
    cacheModelRepository.deleteMany(ids);
    models.forEach(this::create);
  }

  /**
   * @see #deleteManyCached(List)
   */
  public CompletableFuture<?> deleteManyCachedAsync(List<String> ids) {
    return runAsync(() -> deleteManyCached(ids), executor);
  }

  /**
   * @see #deleteManyCached(String...)
   */
  public CompletableFuture<?> deleteManyCachedAsync(String... ids) {
    return deleteManyCachedAsync(Arrays.asList(ids));
  }

  @Override
  public void deleteManyByQueryCached(
    Object query,
    int limit
  ) {
    List<T> models = cacheModelRepository.findManyByQuery(query, limit);
    cacheModelRepository.deleteManyByQuery(query, limit);
    models.forEach(this::create);
  }

  /**
   * @see #deleteManyByQueryCached(Object, int)
   */
  public CompletableFuture<?> deleteManyByQueryCachedAsync(
    Object query,
    int limit
  ) {
    return runAsync(() -> deleteManyByQueryCached(query, limit));
  }

  /**
   * @see #deleteManyByQueryCached(Object)
   */
  public CompletableFuture<?> deleteManyByQueryCachedAsync(Object query) {
    return deleteManyByQueryCachedAsync(query, -1);
  }

  @Override
  public void setCacheRepository(ModelRepository<T> repository) {
    this.cacheModelRepository = repository;
  }
}
