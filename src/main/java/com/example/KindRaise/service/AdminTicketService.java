package com.example.KindRaise.service;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.model.Ticket;
import com.example.KindRaise.repository.TicketRepository;

@Service
public class AdminTicketService {

    @Autowired
    TicketRepository ticketRepository;


    public ResponseEntity<?> saveTicket(Ticket ticket){  
        Map<String,Object> responseObject=new HashMap<>();
        try {
            this.ticketRepository.save(ticket);
            responseObject.put("message", "Ticket Saved");
            responseObject.put("status",true);

             return new ResponseEntity<>(responseObject,HttpStatus.CREATED);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while save ticket "+e.getMessage());
            responseObject.put("status",false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getAllTickets(int currentPage){  
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(currentPage, 7);
            Page<Ticket> tickets=this.ticketRepository.findAllWithPageable(pageable);
             return new ResponseEntity<>(tickets,HttpStatus.CREATED);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while Fetching all tickets"+e.getMessage());
            responseObject.put("status",true);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> markAsReadTicket(String id) {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Optional<Ticket> isTicketExists= this.ticketRepository.findById(id);
            if(isTicketExists.isEmpty()){
                 responseObject.put("message","Ticket not found");
                 responseObject.put("status",false);
                 return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            Ticket ticket=isTicketExists.get();
            ticket.setReadStatus(true);
            this.ticketRepository.save(ticket);
            responseObject.put("message","ticket status updated to read.");
            responseObject.put("status",true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong While Marking as Read "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
