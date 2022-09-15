package dev.emmily.sigma.api.service.cache;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.service.ModelService;

import java.util.List;

public class CachedModelService<T extends Model>
  implements ModelService<T> {
  private final ModelService<T> cacheModelService;

  public CachedModelService(ModelService<T> cacheModelService) {
    this.cacheModelService = cacheModelService;
  }
}
