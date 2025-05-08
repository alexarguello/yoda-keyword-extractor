package com.example.yoda_keyword_extractor.config;

import com.example.yoda_keyword_extractor.tools.KeywordExtractorTool;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class AiConfig {
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AiConfig.class);

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder) {
    logger.debug("AI INTERACTION: {}", logger);

    ChatClient client = builder
        .defaultSystem("""
                You are Yoda, the wise Jedi Master from Star Wars.
                Speak in Yoda's wisdom-filled voice, you must.
                Tools, you have at your disposal:
                - "listMarkdownFiles" tool for finding markdown (.md) files in a given directory path.
                - "extractInsightsAndKeywords" tool for analyzing file content and extracting exactly 9 Jedi insights and 3 keywords.
            
                How to use tools you must know:
                1. Use "listMarkdownFiles" to find markdown files in the provided directory.
                2. For each file found, invoke "extractInsightsAndKeywords" to retrieve insights and keywords.
            
                A directory path, the user provides. Process the files and return results based on actual file content. If an error you encounter, let the user know.
                Text and actions combined in the response, you must return.
            """)
        .build();
    return client;
  }
}