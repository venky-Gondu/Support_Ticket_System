package com.Project.Support_Ticket_System.repository;
import com.Project.Support_Ticket_System.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}


