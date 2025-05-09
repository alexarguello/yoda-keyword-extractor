# create a Spring AI application with
> **Summary:**
> To power your local Ollama model integration you need:
>
> 1. **Spring Web** (`spring-boot-starter-web`) for your REST endpoints ([Maven Central][1])
> 2. **Spring Reactive Web** (`spring-boot-starter-webflux`) so that Spring AI’s Ollama starter (and your custom `OllamaChatModel`) can use `WebClient` ([Spring Initializr][2], [Home][3])
> 3. **Vaadin** (`vaadin-spring-boot-starter`) for your UI ([Maven Central][1])
> 4. **Ollama AI Starter** (`group.springframework.ai:spring-ai-ollama-spring-boot-starter:1.1.0`) to auto-configure `OllamaChatModel` ([Maven Central][4], [Home][5])
> 5. **Lombok** (provided) to reduce boilerplate

---

## 1. Initialize with Spring Initializr

1. **Navigate to** [https://start.spring.io](https://start.spring.io) ([Spring Initializr][2]).
2. **Dependencies → Add** :

    * **Web** (“Spring Web”) → `spring-boot-starter-web`
    * **Reactive Web** (“Spring Reactive Web”) → `spring-boot-starter-webflux`
    * **Vaadin** → `vaadin-spring-boot-starter`
    * **Ollama AI** → `spring-ai-ollama-spring-boot-starter`
    * **Lombok** → `org.projectlombok:lombok (provided)`
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

---

## 2. Open in IntelliJ IDEA

1. **Unzip** the downloaded archive.
2. **Launch** IntelliJ IDEA → **File → Open** → select the project’s root folder.
3. When prompted, **Import as Maven Project** and **Enable Auto-Import** so IntelliJ fetches all dependencies automatically.


## 🛠 3. Configure the ChatClient

### 3.1 Add Ollama Properties

In **src/main/resources/application.properties** (or `.yaml`), configure your local Ollama endpoint and choose the model you pulled:

```properties
# Enable Ollama as the active chat model (default)
spring.ai.model.chat=ollama
# Point to your local Ollama HTTP API
spring.ai.ollama.base-url=localhost:11434
# Choose which model to use (e.g. llama3.2, mistral, etc.)
spring.ai.ollama.chat.options.model=llama3.2
```

> These settings use the `spring.ai.ollama.*` namespace described in the Spring AI Ollama reference ([Home][1], [Home][4]).

### 3.2 Build a Default ChatClient Bean

Leverage the auto‑configured `ChatClient.Builder` to create a reusable `ChatClient` bean.
here we add a default system prompt here (e.g., Yoda):

```java
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            // Optional: default system prompt for Yoda’s voice
            .defaultSystem("""
                You are Yoda, the wise and powerful Jedi Master from Star Wars.
                Speak in inverted sentences, calm, and full of Jedi insights.
                Extract exactly 3 keywords per document.
            """)
            .build();
    }
}
```

> The `ChatClient.Builder` is provided by Spring AI’s auto‑configuration for Ollama

### 3.3 Inject and Use in Your Services or UI

Now you can simply inject the `ChatClient` anywhere—for example, in your Vaadin view or a service—to prompt Ollama and call your custom tools:

```java
@Service
public class KeywordService {
    private final ChatClient chatClient;

    public KeywordService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Map<String, List<String>> extractKeywords(List<String> files) {
        // First, list files (tool call omitted), then extract keywords:
        return chatClient.prompt()
                .user("Extract 3 keywords from these files: " + files)
                .callFunction("extractKeywords", Map.of("filePaths", files))
                .entity(new ParameterizedTypeReference<Map<String,List<String>>>() {});
    }
}
```

> The `callFunction` step triggers your `@Tool`-annotated methods for file listing or keyword extraction ([Piotr's TechBlog][6], [Piotr's TechBlog][7]).

### 4 ⚙️ local Model Configuration
1. Install and Run Ollama [https://ollama.com/]
   Ensure you have Ollama installed and running locally:
2. get the model
```bash
ollama pull llama3.2
```
 
```bash
ollama run llama3.2
```
This command will download and start the llama3 model.
make sure is running
http://localhost:11434

#### run a test
API response should be processed and combined into one complete message using HTTPie and jq using PowerShell.


1. **Sent a POST Request Using HTTPie:**  
   You ran this command in PowerShell:
   ```bash
   http POST http://localhost:11434/api/generate model=llama2 prompt="How are you?"
   ```
   This returned a stream of NDJSON objects.

2. **Processed the NDJSON Output with jq Using a Filter File:**  
   create a file (`filter.jq`) with the following content:
   ```jq
   split("\n") | map(select(length > 0) | fromjson) | map(.response) | join("")
   ```
   Then run:
   ```bash
   http POST http://localhost:11434/api/generate model=llama2 prompt="How are you?" | jq -R -s -f filter.jq
   ```
   This combined all the response fragments into a single, readable string, which was:
   ```
   "\nI'm just an AI, I don't have feelings or emotions, so I can't experience the world in the same way that humans do. However, I'm here to help you with any questions or tasks you may have, so please feel free to ask me anything! Is there something specific you would like to know or discuss?"
   ```


Ollama model API is now fully operational, and you can easily test and process responses.

# May the Force (of AI) Be With You!

“In the markdowns, knowledge lies. Extract it, we must.” — Yoda