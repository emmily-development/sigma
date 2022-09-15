package dev.emmily.sigma.api.service;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public abstract class CachedAsyncModelService<T extends Model>
  extends AsyncModelService<T>
  implements ModelService<T>, CachedModelService<T> {
  private final ModelService<T> cacheModelService;

  public CachedAsyncModelService(
    Executor executor,
    ModelService<T> cacheModelService
  ) {
    super(executor);
    this.cacheModelService = cacheModelService;
  }

  public CachedAsyncModelService(ModelService<T> cacheModelService) {
    this.cacheModelService = cacheModelService;
  }

  @Override
  public void cache(T model) {
    cacheModelService.create(model);
  }

  /**
   * @see #cache(Model)
   */
  public CompletableFuture<?> cacheAsync(T model) {
    return runAsync(() -> cache(model), executor);
  }

  @Override
  public T findCached(String id) {
    return cacheModelService.find(id);
  }

  /**
   * @see #findCached(String)
   */
  public CompletableFuture<T> findCachedAsync(String id) {
    return supplyAsync(() -> findCached(id), executor);
  }

  /**
   * @see #getOrFindCached(String)
   */
  public CompletableFuture<T> getOrFindCachedAsync(String id) {
    return supplyAsync(() -> getOrFindCached(id), executor);
  }

  @Override
  public T findByQueryCached(Object query) {
    return cacheModelService.findByQuery(query);
  }

  /**
   * @see #findByQueryCached(Object)
   */
  public CompletableFuture<T> findByQueryCachedAsync(Object query) {
    return supplyAsync(() -> findByQueryCached(query), executor);
  }

  /**
   * @see #getOrFindByQueryCached(Object)
   */
  public CompletableFuture<T> getOrFindByQueryCachedAsync(Object query) {
    return supplyAsync(() -> getOrFindByQueryCached(query), executor);
  }

  @Override
  public List<T> findManyCached(
    List<String> ids,
    int limit
  ) {
    return cacheModelService.findMany(ids, limit);
  }

  /**
   * @see #findManyCached(List, int)
   */
  public CompletableFuture<List<T>> findManyCachedAsync(
    List<String> ids,
    int limit
  ) {
    return supplyAsync(() -> findManyCached(ids, limit), executor);
  }

  /**
   * @see #findManyCached(List)
   */
  public CompletableFuture<List<T>> findManyCachedAsync(List<String> ids) {
    return findManyCachedAsync(ids, -1);
  }

  /**
   * @see #findManyCached(int, String...)
   */
  public CompletableFuture<List<T>> findManyCachedAsync(
    int limit,
    String... ids
  ) {
    return findManyCachedAsync(Arrays.asList(ids), limit);
  }

  /**
   * @see #findManyCached(String...)
   */
  public CompletableFuture<List<T>> findManyCachedAsync(String... ids) {
    return findManyCachedAsync(-1, ids);
  }

  @Override
  public List<T> findManyByQueryCached(
    Object query,
    int limit
  ) {
    return cacheModelService.findManyByQuery(query, limit);
  }

  /**
   * @see #findManyByQueryCached(Object, int)
   */
  public CompletableFuture<List<T>> findManyByQueryCachedAsync(
    Object query,
    int limit
  ) {
    return supplyAsync(() -> findManyByQueryCached(query, limit));
  }

  /**
   * @see #findManyByQueryCached(Object)
   */
  public CompletableFuture<List<T>> findManyByQueryCachedAsync(Object query) {
    return findManyByQueryCachedAsync(query, -1);
  }

  @Override
  public List<T> findAllCached() {
    return cacheModelService.findAll();
  }

  /**
   * @see #findAllCached()
   */
  public CompletableFuture<List<T>> findAllCachedAsync() {
    return supplyAsync(this::findAllCached, executor);
  }

  @Override
  public void deleteCached(String id) {
    cacheModelService.delete(id);
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
    cacheModelService.deleteByQuery(query);
  }

  /**
   * @see #deleteByQueryCached(Object)
   */
  public CompletableFuture<?> deleteByQueryCachedAsync(Object query) {
    return runAsync(() -> deleteByQueryCached(query), executor);
  }

  @Override
  public void deleteManyCached(List<String> ids) {
    cacheModelService.deleteMany(ids);
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
    cacheModelService.deleteManyByQuery(query, limit);
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
}
