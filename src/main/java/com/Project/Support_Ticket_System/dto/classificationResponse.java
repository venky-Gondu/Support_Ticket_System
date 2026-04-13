package com.Project.Support_Ticket_System.dto;

import com.Project.Support_Ticket_System.entity.Category;
import com.Project.Support_Ticket_System.entity.Priority;

public class classificationResponse {

    private String category;
    private String priority;

    public classificationResponse() {
    }

    public classificationResponse(String category, String priority) {
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
