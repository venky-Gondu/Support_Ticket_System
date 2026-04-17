package com.Project.Support_Ticket_System.service;

import com.Project.Support_Ticket_System.config.LLMProperties;
import com.Project.Support_Ticket_System.dto.LlmClassificationResult;
import com.Project.Support_Ticket_System.dto.classificationResponse;
import com.Project.Support_Ticket_System.entity.Category;
import com.Project.Support_Ticket_System.entity.Priority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
public class LLMService {

    private WebClient webClient;
    private LLMProperties llmProperties;
    @Autowired
    private BuildPrompt buildPrompt;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(LLMService.class);

    public LLMService(
                      LLMProperties llmProperties,
                      ObjectMapper objectMapper) {
        this.webClient = WebClient.create();
        this.llmProperties = llmProperties;
        this.objectMapper = objectMapper; // Injected by Spring
    }


    public classificationResponse classifyTicket(String description){
         if(llmProperties.getApiKey()==null || llmProperties.getApiKey().isEmpty()){
             logger.warn("LLM API Key not configured. Returning null suggestions.");
             return new classificationResponse(null,null);
         }

        try{
            // Build the prompt
            buildPrompt.build();
            String systemPrompt=buildPrompt.getPrompt();
            String userPrompt="Ticket description: " + description;

            // 3. Prepare Request Body for Groq (OpenAI-compatible format)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", llmProperties.getModel());

            List<Map<String, String>> messages = Arrays.asList(
                    createMessage("system", systemPrompt),
                    createMessage("user", userPrompt)
            );
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.1); // Very low for deterministic output
            requestBody.put("max_tokens", 100);   // Small response = faster + cheaper

            // 4. Call Groq API with timeout
            Map<String, Object> apiResponse = webClient.post()
                    .uri(llmProperties.getApiUrl())
                    .header("Authorization", "Bearer " + llmProperties.getApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // Blocking call for simplicity

            if (apiResponse != null && apiResponse.containsKey("choices")) {
                List<?> choices = (List<?>) apiResponse.get("choices");
                if (!choices.isEmpty()) {
                    Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    String content = (String) message.get("content");

                    logger.info("LLM Raw Response {}", content);

                    // Parse JSON with Jackson
                    LlmClassificationResult result = parseLlmResponse(content);

                    if (result != null && result.getCategory() != null && result.getPriority() != null) {
                        String validCategory = validateEnum(result.getCategory(), Category.values());
                        String validPriority = validateEnum(result.getPriority(), Priority.values());
                        return new classificationResponse(validCategory,validPriority);
                    }
                }
            }


        }
     catch (WebClientResponseException e) {
         logger.error("LLM API Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
    } catch (Exception e) {
            logger.error("LLM Service Failed: {}", e.getMessage(), e);
            e.printStackTrace();
    }

        return new classificationResponse(null, null);
    }

    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> msg = new HashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        return msg;
    }

    /**
     * Parse LLM response using Jackson ObjectMapper.
     * Handles markdown code blocks and malformed JSON gracefully.
     */
    private LlmClassificationResult parseLlmResponse(String rawContent) {
        if (rawContent == null || rawContent.isEmpty()) {
            return null;
        }

        try {
            // Clean markdown if LLM adds code blocks
            String cleaned = rawContent.trim();
            if (cleaned.startsWith("```")) {
                // Remove ```json or ``` at start and end
                cleaned = cleaned.replaceAll("^```(json)?\\s*", "").replaceAll("```\\s*$", "").trim();
            }

            // Parse JSON into DTO
            return objectMapper.readValue(cleaned, LlmClassificationResult.class);

        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse LLM JSON: " + e.getMessage());
            System.err.println("Raw content: " + rawContent);
            return null;
        }
    }



    private String validateEnum(String value, Enum[] enums) {
        if (value == null || value.isEmpty()) return null;
        for (Enum e : enums) {
            if (e.name().equalsIgnoreCase(value.trim())) {
                return e.name();
            }
        }
        return null;
    }
}



