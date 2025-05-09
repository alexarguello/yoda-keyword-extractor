package com.example.yoda_keyword_extractor;

import com.example.yoda_keyword_extractor.tools.FileListerTool;
import com.example.yoda_keyword_extractor.tools.KeywordExtractorTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class YodaKeywordExtractorApplicationTests {
	Logger logger = LoggerFactory.getLogger(YodaKeywordExtractorApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	public void test_list_markdown_files_existing_directory() throws IOException {
		// Arrange
		FileListerTool fileListerTool = new FileListerTool();
		Path existingDir = Paths.get("c:/dev");

		// Act
		List<String> result = fileListerTool.listMarkdownFiles(existingDir.toString());

		// Log the found files
		logger.info("Found {} markdown files in directory: {}", result.size(), existingDir);
		result.forEach(file -> logger.info("Found markdown file: {}", file));

		// Assert
		assertTrue(result.stream().allMatch(file -> file.toLowerCase().endsWith(".md")));
		assertTrue(result.stream().anyMatch(file -> file.contains("Butterfly_Garden_for_Beginners.md")));
		assertTrue(result.stream().anyMatch(file -> file.contains("Tips_for_Tech_Talks.md")));
	}

	// Successfully reads content from valid markdown file paths and returns a map with path-to-content mappings
	@Test
	public void test_extract_insights_and_keywords_from_valid_files_with_logging() throws IOException {
		// Arrange
		KeywordExtractorTool extractor = new KeywordExtractorTool();
		String path1 = "C:\\dev\\Butterfly_Garden_for_Beginners.md";
		String path2 = "C:\\dev\\Tips_for_Tech_Talks.md";

		List<String> filePaths = Arrays.asList(path1, path2);

		// Act
		Map<String, String> result = extractor.extractInsightsAndKeywords(filePaths);

		// Assert
		assertEquals(2, result.size());
		assertTrue(result.get(path1).startsWith("#"));
		assertTrue(result.get(path2).startsWith("#"));

		// Log first 3 lines of each file content
		for (String path : filePaths) {
			String content = result.get(path);
			String[] lines = content.split("\n");
			logger.info("First 3 lines of {}: \n{}\n{}\n{}", path, lines[0], lines.length > 1 ? lines[2] : "", lines.length > 2 ? lines[4] : "");
		}
	}
}
