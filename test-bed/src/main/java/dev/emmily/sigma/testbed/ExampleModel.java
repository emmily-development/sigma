package dev.emmily.sigma.testbed;

import dev.emmily.sigma.api.Model;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ExampleModel
  implements Model {
  public static final ExampleModel EMMILY = new ExampleModel("emmily", "example");
  private final String id;
  private final String someProperty;

  @ConstructorProperties({
    "id", "someProperty"
  })
  public ExampleModel(
    String id,
    String someProperty
  ) {
    this.id = id;
    this.someProperty = someProperty;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getSomeProperty() {
    return someProperty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExampleModel that = (ExampleModel) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
