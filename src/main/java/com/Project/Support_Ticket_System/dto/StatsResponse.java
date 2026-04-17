package com.Project.Support_Ticket_System.dto;

import java.util.*;
public class StatsResponse {

    private Long total_tickets;
    private Long open_tickets;
    private Double avg_tickets_per_day;
    private Map<String,Long> priority_breakdown;
    private Map<String,Long> category_breakdown;

    public StatsResponse() {
    }

    public StatsResponse(Long total_tickets, Long open_tickets,
                         Double avg_tickets_per_day,
                         Map<String, Long> priority_breakdown,
                         Map<String, Long> category_breakdown) {
        this.total_tickets = total_tickets;
        this.open_tickets = open_tickets;
        this.avg_tickets_per_day = avg_tickets_per_day;
        this.priority_breakdown = priority_breakdown;
        this.category_breakdown = category_breakdown;
    }

    public Long getTotal_tickets() {
        return total_tickets;
    }

    public void setTotal_tickets(Long total_tickets) {
        this.total_tickets = total_tickets;
    }

    public Long getOpen_tickets() {
        return open_tickets;
    }

    public void setOpen_tickets(Long open_tickets) {
        this.open_tickets = open_tickets;
    }

    public Double getAvg_tickets_per_day() {
        return avg_tickets_per_day;
    }

    public void setAvg_tickets_per_day(Double avg_tickets_per_day) {
        this.avg_tickets_per_day = avg_tickets_per_day;
    }

    public Map<String, Long> getPriority_breakdown() {
        return priority_breakdown;
    }

    public void setPriority_breakdown(Map<String, Long> priority_breakdown) {
        this.priority_breakdown = priority_breakdown;
    }

    public Map<String, Long> getCategory_breakdown() {
        return category_breakdown;
    }

    public void setCategory_breakdown(Map<String, Long> category_breakdown) {
        this.category_breakdown = category_breakdown;
    }
}
