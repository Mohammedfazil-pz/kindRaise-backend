package com.example.KindRaise.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.KindRaise.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,String> {
    
    @Query("""
            SELECT new com.example.KindRaise.model.Ticket(
            t.id,t.firstName,t.lastName,t.phone,t.email,t.readStatus,t.message,t.createdAt
            ) FROM Ticket t ORDER BY t.createdAt DESC
            """)
    Page<Ticket> findAllWithPageable(Pageable pageable);
}
