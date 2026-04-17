package com.Project.Support_Ticket_System.service;

import com.Project.Support_Ticket_System.dto.StatsResponse;
import com.Project.Support_Ticket_System.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.*;

@Service
public class TicketStatsService {
    private final TicketRepository ticketRepository;

    public TicketStatsService(TicketRepository ticketRepository){
        this.ticketRepository=ticketRepository;
    }

    public StatsResponse getStats(){

        Long totalTickets=ticketRepository.countTotalTickets();
        Long openTickets=ticketRepository.countOpenTickets();
        Double avgPerDay=ticketRepository.calculateAvgTicketsPerDay();

        Map<String, Long> priorityBreakdown=new HashMap<>();
        List<Object[]> priorityResults=ticketRepository.getPriorityBreakdown();
        for(Object[] row:priorityResults){
            String priority = row[0].toString();
            Long count = ((Number) row[1]).longValue();
            priorityBreakdown.put(priority.toLowerCase(), count);
        }

        Map<String,Long> categoryBreakdown=new HashMap<>();
        List<Object[]> categoryResults=ticketRepository.getCategoryBreakdown();
        for(Object[] row:categoryResults){
            String category=row[0].toString();
            Long count=((Number) row[1]).longValue();
        }

        return new StatsResponse(totalTickets, openTickets, avgPerDay, priorityBreakdown, categoryBreakdown);

    }

}
