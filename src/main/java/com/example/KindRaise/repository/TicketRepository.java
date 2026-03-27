package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.KindRaise.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,String> {
    
}
