package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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


    public ResponseEntity<?> getAllTickets(){  
        Map<String,Object> responseObject=new HashMap<>();
        try {
            List<Ticket> tickets=this.ticketRepository.findAll();
             return new ResponseEntity<>(tickets,HttpStatus.CREATED);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while Fetching all tickets"+e.getMessage());
            responseObject.put("status",true);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
