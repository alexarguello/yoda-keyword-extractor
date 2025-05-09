package com.example.yoda_keyword_extractor.service;

import com.example.yoda_keyword_extractor.tools.FileListerTool;
import com.example.yoda_keyword_extractor.tools.KeywordExtractorTool;
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

  public Map<String, List<String>> extractKeywords(List<String> files) {
    String prompt = "Extract 3 keywords from these files: " + files;
    return chatClient.prompt()
        .user(prompt)
            .tools(new FileListerTool(), new KeywordExtractorTool())
            .call()
            .entity(new ParameterizedTypeReference<Map<String, List<String>>>() {});
  }
}