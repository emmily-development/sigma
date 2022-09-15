package dev.emmily.sigma.platform.codec.gson;

import com.google.gson.Gson;
import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.codec.ModelCodec;
import team.unnamed.reflect.identity.TypeReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GsonModelCodec
  implements ModelCodec {
  private static final Gson DEFAULT_GSON = new Gson();
  private final Gson gson;

  public GsonModelCodec(Gson gson) {
    this.gson = gson;
  }

  public GsonModelCodec() {
    this(DEFAULT_GSON);
  }

  @Override
  public <T extends Model> String serializeAsString(T model) {
    return gson.toJson(model);
  }

  @Override
  public <T extends Model> byte[] serializeAsBytes(T model) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
      objectOutputStream.writeObject(model);

      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Model> T deserializeFromString(
    String source,
    TypeReference<T> type
  ) {
    return gson.fromJson(source, type.getType());
  }

  @Override
  public <T extends Model> T deserializeFromBytes(
    byte[] source,
    TypeReference<T> type
  ) {
    InputStream inputStream = new ByteArrayInputStream(source);

    try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
      @SuppressWarnings("unchecked")
      T model = (T) objectInputStream.readObject();

      return model;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
