package com.example.yoda_keyword_extractor.service;

import com.example.yoda_keyword_extractor.tools.FileListerTool;
import com.example.yoda_keyword_extractor.tools.FileExtractorTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KeywordService {
  private final ChatClient chatClient;

  public KeywordService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public Map<String, Map<String, List<String>>> extractKeywords(List<String> files) {
    String prompt = "Extract keywords and insights from these markdown files: " + files;
    return chatClient.prompt()
            .user(prompt)
            .call()
            .entity(new ParameterizedTypeReference<Map<String, Map<String, List<String>>>>() {});
  }
}
