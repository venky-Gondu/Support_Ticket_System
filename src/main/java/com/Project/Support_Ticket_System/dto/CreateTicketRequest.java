package com.Project.Support_Ticket_System.dto;

import com.Project.Support_Ticket_System.entity.Category;
import com.Project.Support_Ticket_System.entity.Priority;
import com.Project.Support_Ticket_System.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTicketRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private Priority priority;

//    @NotNull
//    private Status status;

    public CreateTicketRequest() {
    }

    public CreateTicketRequest(Category category, String description, Priority priority, Status status, String title) {
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.title = title;
    }

    public @NotNull Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull Category category) {
        this.category = category;
    }

    public @NotBlank String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank String description) {
        this.description = description;
    }

    public @NotNull Priority getPriority() {
        return priority;
    }

    public void setPriority(@NotNull Priority priority) {
        this.priority = priority;
    }

//    public @NotNull Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(@NotNull Status status) {
//        this.status = status;
//    }

    public @NotBlank @Size(max = 200) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank @Size(max = 200) String title) {
        this.title = title;
    }


}
