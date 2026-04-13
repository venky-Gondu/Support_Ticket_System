package com.Project.Support_Ticket_System.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LLMProperties {

    private final String apiKey;
    private final String apiUrl;
    private final String model;

    @Autowired
    public LLMProperties(
            @Value("${llm.api.key}") String apiKey,
            @Value("${llm.api.url:https://api.groq.com/openai/v1/chat/completions}") String apiUrl,
            @Value("${llm.api.model:llama-3.1-8b-instant}") String model
    ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getModel() {
        return model;
    }
}
