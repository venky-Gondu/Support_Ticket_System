package com.Project.Support_Ticket_System.controller;


import com.Project.Support_Ticket_System.dto.StatsResponse;
import com.Project.Support_Ticket_System.service.TicketStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketStatsController {

    private final TicketStatsService statsService;

    public TicketStatsController(TicketStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
