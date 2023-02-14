package dev.emmily.sigma.platform.json;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.codec.ModelCodec;
import dev.emmily.sigma.api.repository.CachedAsyncModelRepository;
import dev.emmily.sigma.api.repository.ModelRepository;
import team.unnamed.reflect.identity.TypeReference;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JsonModelRepository<T extends Model>
  extends CachedAsyncModelRepository<T>
  implements ModelRepository<T> {
  private static final IllegalArgumentException INVALID_METHOD = new IllegalArgumentException(
    "JsonModelService doesn't support query-based operations"
  );
  private static final FileFilter JSON_FILTER = pathname -> pathname.getName().endsWith(".json");
  private final ModelCodec modelCodec;
  private final File folder;
  private final TypeReference<T> type;

  public JsonModelRepository(
    Executor executor,
    ModelRepository<T> cacheModelRepository,
    ModelCodec modelCodec,
    File folder,
    TypeReference<T> type
  ) {
    super(executor, cacheModelRepository);
    this.modelCodec = modelCodec;
    this.folder = folder;
    if (!folder.exists() && !folder.mkdirs()) {
      throw new RuntimeException("Unable to create the container folder for the model type " +
        type.getTypeName());
    }
    this.type = type;
  }

  public JsonModelRepository(
    ModelRepository<T> cacheModelRepository,
    ModelCodec modelCodec,
    File folder,
    Class<T> type
  ) {
    this(
      Executors.newSingleThreadExecutor(),
      cacheModelRepository,
      modelCodec,
      folder,
      TypeReference.of(type)
    );
  }

  @Override
  @SuppressWarnings("all")
  public void create(T model) {
    File file = new File(folder, model.getId() + ".json");

    if (file.exists() && !file.delete()) {
      throw new RuntimeException("Unable to delete the container file of the " +
        "model " + model.getId());
    }

    try {
      file.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try (FileWriter writer = new FileWriter(file)) {
      writer.write(modelCodec.serializeAsString(model));
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public T find(String id) {
    File file = new File(folder, id + ".json");

    if (!file.exists()) {
      return null;
    }

    return readFromFile(file);
  }

  @Override
  public T findByQuery(Object query) {
    throw INVALID_METHOD;
  }

  @Override
  public List<T> findMany(
    List<String> ids,
    int limit
  ) {
    File[] files = folder.listFiles(pathname -> {
      if (!JSON_FILTER.accept(pathname)) {
        return false;
      }

      return ids.contains(pathname
        .getName()
        .replace(".json", "")
      );
    });

    if (files == null) {
      return Collections.emptyList();
    }

    List<T> models = new ArrayList<>();

    for (File file : files) {
      if (limit-- == 0) {
        break;
      }

      models.add(readFromFile(file));
    }

    return models;
  }

  @Override
  public List<T> findManyByQuery(
    Object query,
    int limit
  ) {
    throw INVALID_METHOD;
  }

  @Override
  public List<T> findAll() {
    File[] files = folder.listFiles(JSON_FILTER);

    if (files == null) {
      return Collections.emptyList();
    }

    List<T> models = new ArrayList<>();

    for (File file : files) {
      models.add(readFromFile(file));
    }

    return models;
  }

  @Override
  public void delete(String id) {
    File file = new File(folder, id + ".json");

    if (file.exists() && !file.delete()) {
      throw new RuntimeException("Unable to delete the container file of the " +
        "model " + id);
    }
  }

  @Override
  public void deleteByQuery(Object query) {
    throw INVALID_METHOD;
  }

  @Override
  @SuppressWarnings("all")
  public void deleteMany(List<String> ids) {
    File[] files = folder.listFiles(pathname -> {
      if (!JSON_FILTER.accept(pathname)) {
        return false;
      }

      return ids.contains(pathname
        .getName()
        .replace(".json", "")
      );
    });

    if (files == null) {
      return;
    }

    for (File file : files) {
      file.delete();
    }

  }

  @Override
  public void deleteManyByQuery(
    Object query,
    int limit
  ) {
    throw INVALID_METHOD;
  }

  private T readFromFile(File file) {
    try {
      return modelCodec.deserializeFromString(String.join(
        "\n",
        Files.readAllLines(Paths.get(file.getPath()))
      ), type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
