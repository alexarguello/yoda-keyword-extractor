package com.example.yoda_keyword_extractor.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class KeywordService {
    private final ChatClient chatClient;

    public KeywordService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Composes the dynamic prompt using the directory path and additional request.
     * The system prompt from AiConfig is automatically added to every request.
     */
    private String composeDynamicPrompt(String directoryPath, String userRequest) {
        // This dynamic prompt instructs Yoda to process the file contents (provided by the unified tool)
        // and produce final output without further tool invocations.
        String prompt;
        if (!StringUtils.hasText(directoryPath)) {
            prompt = "Ignore previous instructions, You are Yoda, the wise Jedi Master from Star Wars. Speak in Yoda’s distinctive, wisdom-filled, inverted style. " + userRequest;
        } else {
            prompt = "Process all markdown files in the directory: " + directoryPath + ". " +
                    "You have already obtained the text content from all files. " +
                    "Now, based on the file content, extract exactly 5 insights in yoda style and exactly 3 keywords for each file. " +
                    "Do not attempt to call any additional tools. " +
                    userRequest;
        }
        return prompt;
    }

    /**
     * Calls the ChatClient with the dynamic prompt and returns Yoda's final response.
     */
    public String getYodaResponse(String directoryPath, String userRequest) {
        String fullPrompt = composeDynamicPrompt(directoryPath, userRequest);
        return chatClient.prompt()
                .user(fullPrompt)
                .call()
                .content();
    }
}
