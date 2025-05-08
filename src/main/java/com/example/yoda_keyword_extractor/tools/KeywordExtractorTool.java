package com.example.yoda_keyword_extractor.tools;

import org.slf4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeywordExtractorTool {
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(KeywordExtractorTool.class);

  @Tool(
      name = "extractInsightsAndKeywords",
      description = "For each provided markdown file path, read the file content and return a map of file path to its content. Use this tool when you need to analyze the content of markdown files for Jedi insights and keywords."
  )
  public Map<String, String> extractInsightsAndKeywords(List<String> filePaths) {
    Map<String, String> result = new LinkedHashMap<>();
    logger.info("Invoked extractInsightsAndKeywords tool for files: {}", filePaths);

    for (String path : filePaths) {
      try {
        logger.debug("Reading file content from: {}", path);
        String content = Files.readString(Paths.get(path));
        result.put(path, content);
      } catch (IOException e) {
        logger.error("Error reading file {}: {}", path, e.getMessage());
        result.put(path, "ERROR: " + e.getMessage());
      }
    }
    return result;
  }
}