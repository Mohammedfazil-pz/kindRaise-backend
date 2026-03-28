package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.model.Ticket;
import com.example.KindRaise.service.AdminTicketService;


@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    AdminTicketService adminTicketService;

    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket){
        System.out.println(ticket);
        return this.adminTicketService.saveTicket(ticket);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTickets(@RequestParam int page){
        return this.adminTicketService.getAllTickets(page);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> markAsReadTicket(@PathVariable String id){
        return this.adminTicketService.markAsReadTicket(id);
    }

   
}
