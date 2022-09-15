package dev.emmily.sigma.api.codec;

import dev.emmily.sigma.api.Model;
import team.unnamed.reflect.identity.TypeReference;

public interface ModelCodec {
  <T extends Model> String serializeAsString(T model);

  <T extends Model> byte[] serializeAsBytes(T model);

  <T extends Model> T deserializeFromString(
    String source,
    TypeReference<T> type
  );

  default <T extends Model> T deserializeFromString(
    String source,
    Class<T> type
  ) {
    return deserializeFromString(source, TypeReference.of(type));
  }

  <T extends Model> T deserializeFromBytes(
    byte[] source,
    TypeReference<T> type
  );

  default <T extends Model> T deserializeFromBytes(
    byte[] source,
    Class<T> type
  ) {
    return deserializeFromBytes(source, TypeReference.of(type));
  }
}
