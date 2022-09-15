package dev.emmily.sigma.api.service.async;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.service.ModelService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncModelService<T extends Model>
  implements ModelService<T> {
  private final ModelService<T> modelService;
  private final Executor executor;

  public AsyncModelService(
    ModelService<T> modelService,
    Executor executor
  ) {
    this.modelService = modelService;
    this.executor = executor;
  }

  public AsyncModelService(ModelService<T> modelService) {
    this(
      modelService,
      Executors.newSingleThreadExecutor()
    );
  }

  @Override
  public void create(T model) {

  }

  public CompletableFuture<?> createAsync(T model) {
    return CompletableFuture.runAsync(() -> create(model));
  }

  @Override
  public T find(String id) {
    return null;
  }

  @Override
  public T findByQuery(Object query) {
    return null;
  }

  @Override
  public List<T> findMany(List<String> ids, int limit) {
    return null;
  }

  @Override
  public List<T> findManyByQuery(Object query, int limit) {
    return null;
  }

  @Override
  public List<T> findAll() {
    return null;
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public void deleteByQuery(Object query) {

  }

  @Override
  public void deleteMany(List<String> ids) {

  }

  @Override
  public void deleteManyByQuery(Object query, int limit) {

  }
}
