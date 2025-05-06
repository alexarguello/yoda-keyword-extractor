
## 1 A. Initialize with Spring Initializr

1. **Navigate to** [https://start.spring.io](https://start.spring.io) ([Spring Initializr][2]).
2. **Project Metadata**

    * **Project**: Maven
    * **Language**: Java
    * **Spring Boot**: 3.4.5
    * **Group**: `com.example`
    * **Artifact**: `yoda-keyword-extractor`
    * **Java**: 17
3. **Dependencies → Add** (type to filter):

    * **Web** (“Spring Web”) → `spring-boot-starter-web` ([Home][3])
    * **Reactive Web** (“Spring Reactive Web”) → `spring-boot-starter-webflux` ([blank][7])
    * **Vaadin** → `vaadin-spring-boot-starter` ([Maven Central][1])
    * **Ollama AI** → `spring-ai-ollama-spring-boot-starter` (v 1.1.0) ([Maven Central][4])
    * **Lombok** → `org.projectlombok:lombok (provided)` ([Stack Overflow][6])
4. **Generate** the project ZIP and **download** it.

### Resulting `pom.xml` Snippet

```xml
<dependencies>
  <!-- 1. Traditional Spring MVC -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <!-- 2. Reactive Web for WebClient support -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>

  <!-- 3. Vaadin Flow UI -->
  <dependency>
    <groupId>com.vaadin</groupId>
    <artifactId>vaadin-spring-boot-starter</artifactId>
    <version>24.7.3</version>
  </dependency>

  <!-- 4. Spring AI Ollama Auto-Configuration -->
  <dependency>
    <groupId>group.springframework.ai</groupId>
    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
    <version>1.1.0</version>
  </dependency>

  <!-- 5. Lombok for boilerplate reduction -->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

> *If your generated POM shows `io.springboot.ai:spring-ai-ollama-spring-boot-starter:1.0.3` instead, that’s a relocated coordinate—either will work (`group.springframework.ai` is the new location) ([Stack Overflow][6]).*

---

## 1 B. Open in IntelliJ IDEA

1. **Unzip** the downloaded archive.
2. **Launch** IntelliJ IDEA → **File → Open** → select the project’s root folder.
3. When prompted, **Import as Maven Project** and **Enable Auto-Import** so IntelliJ fetches all dependencies automatically.

