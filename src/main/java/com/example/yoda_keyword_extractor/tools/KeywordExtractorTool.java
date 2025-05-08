package com.example.yoda_keyword_extractor.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class KeywordExtractorTool {
  
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(KeywordExtractorTool.class);
  private final ChatClient chatClient;

  public KeywordExtractorTool(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @Tool(
      name = "extractInsightsAndKeywords",
      description = "For each provided markdown file path, read the file content and extract exactly 9 unique Jedi insights and 3 keywords that best represent the document."
  )
  public Map<String, Map<String, Object>> extractInsightsAndKeywords(List<String> filePaths) {
    Map<String, Map<String, Object>> result = new LinkedHashMap<>();
    logger.info("Invoked extractInsightsAndKeywords tool for files: {}", filePaths);

    for (String path : filePaths) {
      Map<String, Object> fileResult = new LinkedHashMap<>();
      try {
        logger.debug("Reading file content from: {}", path);
        String content = Files.readString(Paths.get(path));
        String prompt = "From the following markdown document, extract exactly 9 unique Jedi insights (numbered list) and then provide exactly 3 keywords that best represent the document in the format [keyword1, keyword2, keyword3].\n\nDocument:\n" + content;
        logger.debug("Generated prompt for file {}: {}", path, prompt);

        String response = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();
        logger.debug("Received response for file {}: {}", path, response);

        fileResult.put("result", response);
      } catch (IOException e) {
        fileResult.put("error", e.getMessage());
      }
      result.put(path, fileResult);
    }
    return result;
  }
}