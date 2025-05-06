package com.example.yoda_keyword_extractor.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * Created by Alexandra.Arguello on 5/6/2025. Project: yoda-keyword-extractor File:
 * KeywordExtractor.
 */
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
                  "Your responses: wisdom-filled, inverted sentences, calm, " +
                  "Force-teaching, supportive. Extract 3 keywords."
          )
          .user(
              "Pluck top 3 keywords, you must, from this document (%s):\n\n%s".formatted(path, content)
          )
          .call()
          .content();
      // Assume LLM returns comma-separated keywords
      List<String> keywords = List.of(reply.split("\\s*,\\s*"));
      result.put(path, keywords.subList(0, Math.min(3, keywords.size())));
    }
    return result;
  }
}
