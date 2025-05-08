# 🧙‍♂️ Yoda Keyword Extractor — Step-by-Step Guide

*“Extract keywords, you must. Enlightenment, you shall find.”* — Yoda

---

## 1. Initialize the Project

1. **Go to** [start.spring.io](https://start.spring.io)  
2. **Configure**  
   - **Project**: Maven  
   - **Language**: Java  
   - **Spring Boot**: **3.4.5**  
   - **Group**: `com.example`  
   - **Artifact**: `yoda-keyword-extractor`  
   - **Java**: **17**  
3. **Add Dependencies** (type to filter):  
   - **Spring Web** (`spring-boot-starter-web`)  
   - **Spring Reactive Web** (`spring-boot-starter-webflux`)  
   - **Vaadin** (`vaadin-spring-boot-starter`)  
   - **Ollama AI** (`spring-ai-ollama-spring-boot-starter:1.1.0`)  
   - **Lombok** (`org.projectlombok:lombok`, scope `provided`)  
4. **Generate** → Download the ZIP → **Unzip**  
5. **Open** IntelliJ IDEA → **File → Open** → select the project root → **Import as Maven** → **Enable Auto-Import**

---

## 2. Verify Your Dependencies

In your generated **pom.xml**, ensure these entries exist under `<dependencies>`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
  <groupId>com.vaadin</groupId>
  <artifactId>vaadin-spring-boot-starter</artifactId>
  <version>24.7.3</version>
</dependency>
<dependency>
  <groupId>group.springframework.ai</groupId>
  <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
  <version>1.1.0</version>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.24</version>
  <scope>provided</scope>
</dependency>
```

### 3. Define the FileLister Tool

Create src/main/java/com/example/yoda/tool/FileLister.java:

```java
package com.example.yoda.tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.Tool;

@Component
public class FileLister {

    @Tool(
      name = "listMdFiles",
      description = "Lists all .md files in the specified directory and subdirectories."
    )
    public List<String> listMdFiles(String directoryPath) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(directoryPath))) {
            return walk
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().toLowerCase().endsWith(".md"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }
}
```

## 4. Define the KeywordExtractor Tool (Yoda Persona)

Create src/main/java/com/example/yoda/tool/KeywordExtractor.java:

```java
package com.example.yoda.tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.tool.Tool;

@Component
public class KeywordExtractor {

    private final ChatClient chatClient;

    public KeywordExtractor(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Tool(
      name = "extractKeywords",
      description = "Extracts the top 3 keywords from the given Markdown documents as Yoda would."
    )
    public Map<String, List<String>> extractKeywords(List<String> filePaths) throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        for (String path : filePaths) {
            String content = Files.readString(Paths.get(path));
            String reply = chatClient.prompt()
                .system(
                  "You are Yoda, the wise and powerful Jedi Master from Star Wars. " +
                  "Speak in inverted sentences, calm and wise. Extract exactly 3 keywords."
                )
                .user(
                  String.format("Pluck top 3 keywords, you must, from this document (%s):\n\n%s", 
                                path, content)
                )
                .call()
                .content();

            // split on commas or whitespace, take first 3
            List<String> keywords = List.of(reply.split("\\s*,\\s*"));
            result.put(path, keywords.subList(0, Math.min(3, keywords.size())));
        }
        return result;
    }
}
```

## 5. Configure Your Ollama Connection

Edit src/main/resources/application.properties (or .yaml):
```properties
spring.application.name=yoda-keyword-extractor

# Use Ollama as the AI model
spring.ai.model.chat=ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3
spring.ai.ollama.chat.options.temperature=0.7

# (Optional) default directory if you choose to read from properties
app.markdown.directory=/path/to/markdown
```
## 6. Wire Up the ChatClient

Create src/main/java/com/example/yoda/config/AiConfig.java:
```java
package com.example.yoda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.ChatClient;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("""
                You are Yoda, the wise and powerful Jedi Master from Star Wars.
                Speak in inverted sentences, calm and full of Jedi insights.
                Extract exactly 3 keywords per document.
            """)
            .build();
    }
}
```
    Spring AI’s Ollama starter auto-configures the underlying OllamaChatModel pointing at localhost:11434.

## 7. Build the Vaadin UI

Create src/main/java/com/example/yoda/ui/MainView.java:
```java
package com.example.yoda.ui;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.springframework.ai.chat.ChatClient;
import org.springframework.core.ParameterizedTypeReference;

@Route("")
public class MainView extends VerticalLayout {

    public MainView(ChatClient chatClient) {
        TextField folder = new TextField("Markdown Folder Path");
        Button askYoda = new Button("Ask Yoda");
        Grid<Map.Entry<String, List<String>>> grid = new Grid<>();

        grid.addColumn(Map.Entry::getKey).setHeader("File");
        grid.addColumn(e -> e.getValue().toString()).setHeader("Keywords");

        askYoda.addClickListener(e -> {
            // Step 1: list files
            List<String> files = chatClient.prompt()
                .callFunction("listMdFiles", Map.of("directoryPath", folder.getValue()))
                .contentAs(List.class);

            // Step 2: extract keywords
            Map<String, List<String>> kws = chatClient.prompt()
                .callFunction("extractKeywords", Map.of("filePaths", files))
                .entity(new ParameterizedTypeReference<Map<String, List<String>>>() {});

            grid.setItems(kws.entrySet());
        });

        add(folder, askYoda, grid);
    }
}
```
## 8. Run & Test

    1. Start Ollama (if not already running):

```bash
ollama run llama3
```
2. Build & Run your Spring Boot app:
   
```bash
    ./mvnw spring-boot:run
```
   3.  Open your browser at http://localhost:8080.
   4.  Enter the path to your .md files → Ask Yoda → watch the grid fill with file names and Yoda-styled keywords!

# May the Force (of AI) Be With You!

“In the markdowns, knowledge lies. Extract it, we must.” — Yoda
