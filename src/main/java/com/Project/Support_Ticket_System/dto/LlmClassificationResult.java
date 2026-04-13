package com.Project.Support_Ticket_System.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LlmClassificationResult {
    @JsonProperty("category")
    private String category;
    @JsonProperty("priority")
    private String priority;

    public LlmClassificationResult() {
    }

    public LlmClassificationResult(String category, String priority) {
        this.category = category;
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
