package com.example.yoda_keyword_extractor.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CombinedMarkdownContentTool {
    private static final Logger logger = LoggerFactory.getLogger(CombinedMarkdownContentTool.class);

    @Tool(
            name = "extractAllMarkdownFiles",
            description = "Scans the specified directory path for Markdown (.md) files and extracts the complete text content of each file. Returns a map where each key is the file path and each value is the file's content. If the directory is invalid or a file cannot be read, an error message is provided for that file."
    )
    public Map<String, String> extractAllMarkdownFiles(String directoryPath) throws IOException {
        logger.info("Invoked extractAllMarkdownFiles tool with directoryPath: {}", directoryPath);

        Path dir = Paths.get(directoryPath);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IOException("Invalid directory path: " + directoryPath);
        }

        // List markdown files
        List<String> markdownFiles;
        try (var paths = Files.list(dir)) {
            markdownFiles = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(file -> file.toLowerCase().endsWith(".md"))
                    .collect(Collectors.toList());

            logger.debug("Markdown files found: {}", markdownFiles);
        } catch (IOException e) {
            logger.error("Error while listing files in directory: {}", directoryPath, e);
            throw e;
        }

        // Extract file content for each markdown file found
        Map<String, String> result = new LinkedHashMap<>();
        for (String filePath : markdownFiles) {
            try {
                logger.debug("Reading file content from: {}", filePath);
                String content = Files.readString(Paths.get(filePath));
                result.put(filePath, content);
            } catch (IOException e) {
                logger.error("Error reading file {}: {}", filePath, e.getMessage());
                result.put(filePath, "ERROR: " + e.getMessage());
            }
        }
        return result;
    }
}
