package com.example.yoda_keyword_extractor.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Deprecated
@Service
public class KeywordOrchestratorService {

  private final ChatClient chatClient;

  public KeywordOrchestratorService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  /**
   * For each file, reads its content and extracts 9 Jedi insights and 3 keywords.
   * Optionally, includes any extra user request in the prompt.
   * @param filePaths List of markdown file paths
   * @param userRequest Additional user request (can be empty)
   * @return Map of file name to extraction result (insights, keywords, etc.)
   */
  public Map<String, String> extractInsightsAndKeywords(List<String> filePaths, String userRequest) {
    Map<String, String> results = new LinkedHashMap<>();
    for (String filePath : filePaths) {
      try {
        String content = Files.readString(Paths.get(filePath));
        String prompt = String.format(
            "%s\n\nExtract 9 Jedi insights from this document and at the end provide the 3 most relevant keywords in the format [keyword 1, keyword 2, keyword 3].\n\nDocument content:\n%s",
            userRequest != null && !userRequest.isBlank() ? userRequest : "",
            content
        );
        String response = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();
        results.put(filePath, response);
      } catch (Exception e) {
        results.put(filePath, "Error: " + e.getMessage());
      }
    }
    return results;
  }
}