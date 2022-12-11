package dev.emmily.sigma.api.repository;

import dev.emmily.sigma.api.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This is an abstract implementation of
 * {@link ModelRepository} which provides async
 * versions of the existing methods using
 * {@link Executor executors} and
 * {@link CompletableFuture completable futures}.
 * 
 * @param <T> The type of model held by this
 *           model service.
 */
public abstract class AsyncModelRepository<T extends Model>
  implements ModelRepository<T> {
  protected final Executor executor;

  public AsyncModelRepository(Executor executor) {
    this.executor = executor;
  }
  
  public AsyncModelRepository() {
    this(Executors.newSingleThreadExecutor());
  }

  /**
   * @see #create(Model) 
   */
  public CompletableFuture<?> createAsync(T model) {
    return runAsync(() -> create(model), executor);
  }

  /**
   * @see #find(String) 
   */
  public CompletableFuture<T> findAsync(String id) {
    return supplyAsync(() -> find(id), executor);
  }

  /**
   * @see #findByQuery(Object) 
   */
  public CompletableFuture<T> findByQueryAsync(Object query) {
    return supplyAsync(() -> findByQuery(query), executor);
  }

  /**
   * @see #findMany(List, int)
   */
  public CompletableFuture<List<T>> findManyAsync(
    List<String> ids,
    int limit
  ) {
    return supplyAsync(() -> findMany(ids, limit), executor);
  }

  /**
   * @see #findMany(List)
   */
  public CompletableFuture<List<T>> findManyAsync(List<String> ids) {
    return findManyAsync(ids, -1);
  }

  /**
   * @see #findMany(int, String...)
   */
  public CompletableFuture<List<T>> findManyAsync(
    int limit,
    String... ids
  ) {
    return findManyAsync(Arrays.asList(ids), limit);
  }

  /**
   * @see #findMany(String...)
   */
  public List<T> findMany(String... ids) {
    return findMany(-1, ids);
  }

  /**
   * @see #findManyByQuery(Object, int)
   */
  public CompletableFuture<List<T>> findManyByQueryAsync(
    Object query,
    int limit
  ) {
    return supplyAsync(() -> findManyByQuery(query, limit), executor);
  }

  /**
   * @see #findManyByQuery(Object)
   */
  public CompletableFuture<List<T>> findManyByQueryAsync(Object query) {
    return findManyByQueryAsync(query, -1);
  }

  /**
   * @see #findAll()
   */
  public CompletableFuture<List<T>> findAllAsync() {
    return supplyAsync(this::findAll, executor);
  }

  /**
   * @see #delete(String)
   */
  public CompletableFuture<?> deleteAsync(String id) {
    return runAsync(() -> delete(id), executor);
  }

  /**
   * @see #delete(Model)
   */
  public CompletableFuture<?> deleteAsync(T model) {
    return deleteAsync(model.getId());
  }

  /**
   * @see #deleteByQuery(Object)
   */
  public CompletableFuture<?> deleteByQueryAsync(Object query) {
    return runAsync(() -> deleteByQuery(query), executor);
  }

  /**
   * @see #deleteMany(List)
   */
  public CompletableFuture<?> deleteManyAsync(List<String> ids) {
    return runAsync(() -> deleteMany(ids), executor);
  }

  /**
   * @see #deleteMany(String...)
   */
  public CompletableFuture<?> deleteManyAsync(String... ids) {
    return deleteManyAsync(Arrays.asList(ids));
  }

  /**
   * @see #deleteManyByQuery(Object, int)
   */
  public CompletableFuture<?> deleteManyByQueryAsync(
    Object query,
    int limit
  ) {
    return runAsync(() -> deleteManyByQuery(query, limit));
  }

  /**
   * @see #deleteManyByQuery(Object)
   */
  public CompletableFuture<?> deleteManyByQueryAsync(Object query) {
    return deleteManyByQueryAsync(query, -1);
  }
}
