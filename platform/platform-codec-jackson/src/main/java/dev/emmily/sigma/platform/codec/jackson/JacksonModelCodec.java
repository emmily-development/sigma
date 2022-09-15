package dev.emmily.sigma.platform.codec.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.api.codec.ModelCodec;
import team.unnamed.reflect.identity.TypeReference;

import java.io.IOException;

public class JacksonModelCodec
  implements ModelCodec {
  private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
  private final ObjectMapper objectMapper;

  public JacksonModelCodec(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JacksonModelCodec() {
    this(DEFAULT_OBJECT_MAPPER);
  }

  @Override
  public <T extends Model> String serializeAsString(T model) {
    try {
      return objectMapper.writeValueAsString(model);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Model> byte[] serializeAsBytes(T model) {
    try {
      return objectMapper.writeValueAsBytes(model);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Model> T deserializeFromString(
    String source,
    TypeReference<T> type
  ) {
    try {
      return objectMapper.readValue(
        source,
        objectMapper.getTypeFactory().constructType(type.getType())
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T extends Model> T deserializeFromBytes(
    byte[] source,
    TypeReference<T> type
  ) {
    try {
      return objectMapper.readValue(
        source,
        objectMapper.getTypeFactory().constructType(type.getType())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
