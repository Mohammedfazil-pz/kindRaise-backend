package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.TransactionDto;
import com.example.KindRaise.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;


     Map<String,Object> responseObject=new HashMap<>();

     public ResponseEntity<?> getAllTransactions() {
        try {
            List<TransactionDto> donations=this.transactionRepository.findAllTransactionsByDto();
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch All donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
