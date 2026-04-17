package com.Project.Support_Ticket_System.service;

import com.Project.Support_Ticket_System.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class TicketCleanupService {
    private final TicketRepository ticketRepository;
    private static final Logger logger = LoggerFactory.getLogger(TicketCleanupService.class);

    public TicketCleanupService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredTickets(){
        LocalDateTime cutoff = LocalDateTime.now().minusDays(45);
        int deleted = ticketRepository.deleteByCreatedAtBefore(cutoff);

    }



}
