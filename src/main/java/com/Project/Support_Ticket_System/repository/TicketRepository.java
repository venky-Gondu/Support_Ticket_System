package com.Project.Support_Ticket_System.repository;
import com.Project.Support_Ticket_System.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>{

// custom query for support combined filters (?category=&status=&search=) efficiently,

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:category IS NULL OR t.category = :category) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:searchTerm IS NULL OR t.title LIKE %:searchTerm% OR " +
            "                    t.description LIKE %:searchTerm%)")
    List<Ticket> findTickets(
            @Param("category") Category category,
            @Param("priority") Priority priority,
            @Param("status") Status status,
            @Param("searchTerm") String searchTerm
    );


    // === Stats Aggregation Queries (New) ===

    // 1. Total Tickets Count
    @Query("SELECT COUNT(t) FROM Ticket t")
    Long countTotalTickets();

    // 2. Open Tickets Count
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'OPEN'")
    Long countOpenTickets();

    // 3. Average Tickets Per Day (PostgreSQL native query)
// 3. Average Tickets Per Day (PostgreSQL-compatible)
    @Query(value = "SELECT CASE " +
            "WHEN COUNT(*) = 0 THEN 0.0 " +
            "WHEN MAX(created_at) = MIN(created_at) THEN CAST(COUNT(*) AS NUMERIC) " +
            "ELSE CAST(COUNT(*) AS NUMERIC) / NULLIF(CAST(EXTRACT(EPOCH FROM (MAX(created_at) - MIN(created_at))) / 86400 AS NUMERIC), 0.0) " +
            "END FROM tickets",
            nativeQuery = true)
    Double calculateAvgTicketsPerDay();


    // 4. Priority Breakdown (GROUP BY)
    @Query("SELECT t.priority, COUNT(t) FROM Ticket t GROUP BY t.priority")
    List<Object[]> getPriorityBreakdown();

    // 5. Category Breakdown (GROUP BY)
    @Query("SELECT t.category, COUNT(t) FROM Ticket t GROUP BY t.category")
    List<Object[]> getCategoryBreakdown();


    @Modifying
    @Query("DELETE FROM Ticket t WHERE t.createdAt < :cutoffDate")
    int deleteByCreatedAtBefore(LocalDateTime cutoff);
}


