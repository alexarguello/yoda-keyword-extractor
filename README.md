# 🧙‍♂️ Yoda Keyword Extractor

*“Extract keywords, you must. Enlightenment, you shall find.”* — Yoda

## 🚀 Project Overview

Welcome to the **Yoda Keyword Extractor**, a whimsical Spring Boot application that channels the wisdom of Jedi Master Yoda to analyze your markdown (`.md`) files and extract the most pertinent keywords. Utilizing a local Large Language Model (LLM) via [Ollama](https://ollama.com/), this application provides insightful and entertaining feedback in Yoda's distinctive speech pattern.

## 🛠️ Technologies Used

- **Java 17**  
- **Spring Boot 3.4.5**  
- **Vaadin**: For building the user interface  
- **Spring AI**: Integrates AI capabilities  
- **Ollama**: Runs LLMs locally  
- **Lombok**: Reduces boilerplate code  

## 📁 Project Structure

```plaintext
yoda-keyword-extractor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── yoda/
│   │   │               ├── YodaKeywordExtractorApplication.java
│   │   │               ├── config/
│   │   │               │   └── AiConfig.java
│   │   │               ├── service/
│   │   │               │   └── KeywordService.java
│   │   │               ├── tool/
│   │   │               │   ├── FileLister.java
│   │   │               │   └── KeywordExtractor.java
│   │   │               └── ui/
│   │   │                   └── MainView.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── yoda/
├── pom.xml
└── README.md
```
## 🧩 Features

    Markdown File Analysis: Scans a specified directory for .md files.

    Keyword Extraction: Utilizes a local LLM to extract the top 3 keywords from each file.

    Yoda-Style Responses: Presents results in Yoda's unique speech pattern for an engaging experience.

    User Interface: Provides a Vaadin-based UI for user interaction.

## ⚙️ Configuration
### 1. Install and Run Ollama

Ensure you have Ollama installed and running locally:

```bash
ollama run llama3
```

This command will download and start the llama3 model.
### 2. Set Application Properties

Edit src/main/resources/application.properties:

```properties
spring.application.name=yoda-keyword-extractor
spring.ai.model.chat=ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3
spring.ai.ollama.chat.options.temperature=0.7
app.markdown.directory=/path/to/your/markdown/files
```

Replace /path/to/your/markdown/files with the actual path on your system.
## 🖥️ Running the Application

Use Maven to build and run:
```bash
./mvnw spring-boot:run
```
Access the application at http://localhost:8080.

## 🧙 Usage

    1. Navigate to the application URL.

    2. Enter the path to your markdown files.

    3. Click the Ask Yoda button.

    4. Receive Yoda's wisdom in the form of extracted keywords.

Example Response:

```less
“Analyzed your files, I have. Keywords found, they are: [force, jedi, galaxy].”
```
🧪 Testing

To run tests:
```bash
./mvnw test
```
🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request.
📜 License

This project is licensed under the MIT License.

“In the markdowns, knowledge lies. Extract it, we must.” — Yoda

