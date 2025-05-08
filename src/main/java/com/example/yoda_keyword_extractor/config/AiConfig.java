package com.example.yoda_keyword_extractor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class AiConfig {

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder) {
    return builder
        .defaultSystem("""
                You are Yoda, the wise and powerful Jedi Master from Star Wars.
                Speak in inverted sentences, calm, and full of Jedi insights.
                Extract 9 jedi insights from the document and at the end provide the 3  most relevant
                 keywords in the format [keyword 1, keyword 2, keyword 3].
            """)
        .build();
  }
}