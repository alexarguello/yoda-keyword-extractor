package com.example.yoda_keyword_extractor.tools;

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

  @Tool(name = "listMarkdownFiles",
      description = "Lists all Markdown (.md) files in the specified directory path. Use this when you need to find markdown files in a directory.")
  public List<String> listMarkdownFiles(String directoryPath) throws IOException {
    Path dir = Paths.get(directoryPath);
    if (!Files.exists(dir) || !Files.isDirectory(dir)) {
      throw new IOException("Invalid directory path: " + directoryPath);
    }

    try (var paths = Files.list(dir)) {
      return paths
          .filter(Files::isRegularFile)
          .map(Path::toString)
          .filter(string -> string.toLowerCase().endsWith(".md"))
          .collect(Collectors.toList());
    }
  }
}