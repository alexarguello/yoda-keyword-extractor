package com.example.yoda_keyword_extractor.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class KeywordExtractorTool {

  private final ChatClient chatClient;

  public KeywordExtractorTool(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @Tool(name = "extractKeywords",
      description = "Extracts the top 3 keywords from each of the provided markdown files. Use this when you need to analyze the content of markdown files.")
  public Map<String, List<String>> extractKeywords(List<String> filePaths) {
    Map<String, List<String>> result = new HashMap<>();

    for (String path : filePaths) {
      try {
        String content = Files.readString(Paths.get(path));

        // Use the ChatClient to extract keywords
        String response = chatClient.prompt()
            .system("Extract exactly 3 keywords that best represent this document. Return only the keywords separated by commas.")
            .user(content)
            .call()
            .content();

        // Parse comma-separated keywords
        List<String> keywords = Arrays.stream(response.split(","))
            .map(String::trim)
            .limit(3)
            .collect(Collectors.toList());

        result.put(path, keywords);

      } catch (IOException e) {
        result.put(path, List.of("Error: " + e.getMessage()));
      }
    }

    return result;
  }
}