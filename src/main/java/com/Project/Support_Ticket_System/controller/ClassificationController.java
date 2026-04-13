package com.Project.Support_Ticket_System.controller;

import com.Project.Support_Ticket_System.dto.classificationRequest;
import com.Project.Support_Ticket_System.dto.classificationResponse;
import com.Project.Support_Ticket_System.service.LLMService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class ClassificationController {
    private final LLMService llmService;

    public ClassificationController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/classify")
    public ResponseEntity<classificationResponse> classifyTicket(@Valid @RequestBody classificationRequest request) {
        classificationResponse response = llmService.classifyTicket(request.getDescription());
        return ResponseEntity.ok(response);
    }

}
