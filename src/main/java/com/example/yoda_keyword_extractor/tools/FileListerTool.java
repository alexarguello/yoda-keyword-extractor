package com.example.yoda_keyword_extractor.tools;

import org.slf4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileListerTool {
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FileListerTool.class);
  
  @Tool(
      name = "listMarkdownFiles",
      description = "Lists all Markdown (.md) files in the specified directory path. Use this when you need to find markdown files in a directory.")
  public List<String> listMarkdownFiles(String directoryPath) throws IOException {
    logger.info("Invoked listMarkdownFiles tool with directoryPath: {}", directoryPath);

    Path dir = Paths.get(directoryPath);
    if (!Files.exists(dir) || !Files.isDirectory(dir)) {
      throw new IOException("Invalid directory path: " + directoryPath);
    }

    try (var paths = Files.list(dir)) {
      List<String> markdownFiles = paths
          .filter(Files::isRegularFile)
          .map(Path::toString)
          .filter(string -> string.toLowerCase().endsWith(".md"))
          .collect(Collectors.toList());

      logger.debug("Markdown files found: {}", markdownFiles);
      return markdownFiles;
    } catch (IOException e) {
      logger.error("Error while listing files in directory: {}", directoryPath, e);
      throw e;
    }

  }
}