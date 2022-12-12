# Sigma

Sigma is a storage library providing a SPI for handling CRUD operations.
The project is divided in a set of modules, the API module, which you can
use to create your own implementations, and the built-in implementations.

-----

## Installation

In order to use the library you must include the API module in your project
classpath and (optionally) one or more implementation modules. To do so,
you can use tools like Maven or Gradle.

### Maven

#### Repository

```xml
<repositories>
  <repository>
    <id>emmily-public</id>
    <url>https://repo.emmily.dev/repository/emmily-public</url>
  </repository>
</repositories>
```
#### Dependency

```xml
<dependencies>
  <dependency>
    <groupId>dev.emmily</groupId>
    <artifactId>sigma-api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

### Gradle

#### Kotlin DSL

```kotlin
repositories {
  maven("https://repo.emmily.dev/repository/emmily-public")
}

dependencies {
  implementation("dev.emmily:sigma-api:1.0.0-SNAPSHOT")  
}
```

#### Groovy DSL

```groovy
repositories {
  maven { url 'https://repo.emmily.dev/repository/emmily-public' }
}

dependencies {
  implementation 'dev.emmily:sigma-api:1.0.0-SNAPSHOT'
}
```

----

## Usage

The main two concepts of Sigma are the repository and the model. A model
is anything that contains data intended to be savable, i.e., a user; while
a repository is where you save these models. Following the User example
we can create this code:

### Our model

```java
package dev.emmily.sigma.test;

import dev.emmily.sigma.api.Model;

public class User
  implements Model {
  private final String id;
  private String username;
  private final long registerDate;

  @ConstructorProperties({
    "id", "username",
    "registerDate"
  })
  public User(
    String id,
    String username,
    long registerDate
  ) {
    this.id = id;
    this.username = username;
    this.registerDate = registerDate;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public long getRegisterDate() {
    return registerDate;
  }
}
```

### Our test

```java
package dev.emmily.sigma.test;

import dev.emmily.sigma.api.Model;
import dev.emmily.sigma.platform.jdk.MapModelRepository;
import dev.emmily.sigma.platform.json.JsonModelRepository;
import dev.emmily.sigma.platform.codec.gson.GsonModelCodec;

import java.io.File;

public class SigmaTest {
  public static void main(String[] args) {
    ModelRepository<User> userCache = new MapModelRepository<>();
    CachedAsyncModelRepository<User> userRepository = new JsonModelRepository<>(
      userCache,
      new GsonModelCodec(),
      new File("./users/"),
      User.class
    );
    
    // The user is both cached and saved in a JSON file
    userRepository.create(new User(
      "emmily",
      "Emmily Sophia",
      System.currentTimeMillis()
    ););

    // Get a model with the id "emmily" from the cache repository
    // and print its username
    System.out.println(userRepository.get("emmily").getUsername());
    
    // Delete the model with the id "emmily" from the cache repository
    userRepository.deleteCached("emmily");
    
    // Prints true
    System.out.println(userRepository.get("emmily") == null);
    
    // Prints "Emmily Sophia"
    System.out.println(userRepository.find("emmily").getUsername());
    
    // Finds the file named "emmily.json" and saves the model
    // in the cache repository.
    userRepository.cache(userRepository.find("emmily"));
    
    // Deletes the JSON file of the model "emmily"
    userRepository.delete("emmily");

    // Prints true
    System.out.println(userRepository.find("emmily") == null);

    // Prints false
    System.out.println(userRepository.get("emmily") == null);
    
    userRepository.get("emmily").setUsername("Emmily Development");
    
    userRepository.create(userRepository.get("emmily"));
    
    userRepository.deleteCached("emmily");

    // Prints "Emmily Development" instead of "Emmily Sophia"
    // because it was modified and replaced in the JSON file
    System.out.println(userRepository.find("emmily").getUsername());
  }
}
```

-----
