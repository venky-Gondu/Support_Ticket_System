package com.Project.Support_Ticket_System.dto;

import jakarta.validation.constraints.NotNull;

public class classificationRequest {

    @NotNull
    private String description;

    public classificationRequest() {
    }

    public @NotNull String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }
}
