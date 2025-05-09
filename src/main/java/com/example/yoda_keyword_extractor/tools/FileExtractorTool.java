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
public class FileExtractorTool {
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FileExtractorTool.class);

  @Tool(
          name = "extractFilesContent",
          description = "Extracts the textual content from a list of file paths. Returns a map where keys are file paths and values are the complete text content of each file. Use this tool when you need to analyze, process, or extract information from the content of specific files. If a file cannot be read, an error message will be included in the map instead of content."
  )
  public Map<String, String> extractFilesContent(List<String> filePaths) {
    Map<String, String> result = new LinkedHashMap<>();
    logger.info("Invoked extractFilesContent tool for files: {}", filePaths);

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