package com.Project.Support_Ticket_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    public Ticket() {
    }

    public Ticket(Category category, LocalDateTime createdAt, String description, Long id, Priority priority, Status status, String title) {
        this.category = category;
        this.createdAt = createdAt;
        this.description = description;
        this.id = id;
        this.priority = priority;
        this.status = status;
        this.title = title;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt;}

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(Status status) { this.status = status; }

    //JPA Lifecycle Callback (Auto-set created_at and default status)
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.OPEN;
        }
    }

    // to String Method
    @Override
    public String toString() {
        return "Ticket{" +
                "category=" + category +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", createdAt=" + createdAt +
                '}';
    }
}
