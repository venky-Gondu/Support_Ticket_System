package com.Project.Support_Ticket_System.controller;

import com.Project.Support_Ticket_System.dto.CreateTicketRequest;
import com.Project.Support_Ticket_System.dto.TicketResponse;
import com.Project.Support_Ticket_System.dto.UpdateTicketRequest;
import com.Project.Support_Ticket_System.entity.Category;
import com.Project.Support_Ticket_System.entity.Priority;
import com.Project.Support_Ticket_System.entity.Status;
import com.Project.Support_Ticket_System.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/tickets/")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    // Post Mapping for Creating Ticket.
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request){
        TicketResponse response=ticketService.creteTicket(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false)Status status,
            @RequestParam(required = false) String search
            )
    {
        List<TicketResponse> tickets = ticketService.getAllTickets(category, priority, status, search);
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200
    }

    // GET /api/tickets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicketById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH /api/tickets/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable Long id, @Valid @RequestBody UpdateTicketRequest request) {
        TicketResponse response = ticketService.updateTicket(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
