package com.example.yoda_keyword_extractor.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * Created by Alexandra.Arguello on 5/6/2025. Project: yoda-keyword-extractor File: FileLister.
 */
@Component
public class FileLister {

  @Tool(
      name = "listMdFiles",
      description = "Lists all .md files in the specified directory and subdirectories."
  )
  public List<String> listMdFiles(String directoryPath) throws IOException {
    try (Stream<Path> walk = Files.walk(Paths.get(directoryPath))) {
      return walk
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().endsWith(".md"))
          .map(Path::toString)
          .collect(Collectors.toList());
    }
  }
}
