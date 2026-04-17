package com.Project.Support_Ticket_System.service;

import com.Project.Support_Ticket_System.dto.CreateTicketRequest;
import com.Project.Support_Ticket_System.dto.TicketResponse;
import com.Project.Support_Ticket_System.dto.UpdateTicketRequest;
import com.Project.Support_Ticket_System.entity.Category;
import com.Project.Support_Ticket_System.entity.Priority;
import com.Project.Support_Ticket_System.entity.Status;
import com.Project.Support_Ticket_System.entity.Ticket;
import com.Project.Support_Ticket_System.exception.ResourceNotFoundException;
import com.Project.Support_Ticket_System.repository.TicketRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    // constructor Injection
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketResponse creteTicket(CreateTicketRequest request){
        Ticket ticket=new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        // Status defaults to OPEN in Entity @PrePersist

        Ticket saved = ticketRepository.save(ticket);
        return mapToResponse(saved);
    }

    private TicketResponse mapToResponse(Ticket saved) {
        return new TicketResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getCategory(),
                saved.getPriority(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets(Category category, Priority priority, Status status, String search) {
        String searchTerm = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        List<Ticket> tickets = ticketRepository.findTickets(category, priority, status, searchTerm);
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).reversed());
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket not found with id: " + id));
        return mapToResponse(ticket);
    }

    // Write Operation (Default: readOnly = false)
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        if (request.getTitle() != null) ticket.setTitle(request.getTitle());
        if (request.getDescription() != null) ticket.setDescription(request.getDescription());
        if (request.getCategory() != null) ticket.setCategory(request.getCategory());
        if (request.getPriority() != null) ticket.setPriority(request.getPriority());
        if (request.getStatus() != null) ticket.setStatus(request.getStatus());

        Ticket updated = ticketRepository.save(ticket);
        return mapToResponse(updated);
    }





}
