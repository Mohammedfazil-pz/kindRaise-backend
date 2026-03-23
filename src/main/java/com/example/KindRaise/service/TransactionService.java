package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
          Map<String,Object> responseObject=new HashMap<>();
        try {
            List<TransactionDto> donations=this.transactionRepository.findAllTransactionsByDto();
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch All Transactions "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUserTransactions(String userId,int currentPage,int size){
        Map<String,Object> responseObject=new HashMap<>();
        try {

            Pageable page=PageRequest.of(currentPage, size);

            Page<TransactionDto> userTransactions=this.transactionRepository.findUserTransactionsWithDto(userId,page);

            return new ResponseEntity<>(userTransactions,HttpStatus.OK);
            
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch User Transactions "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


     public ResponseEntity<?> getUserAllTransactionsInOneCall(String userId){
        Map<String,Object> responseObject=new HashMap<>();
        try {

            List<TransactionDto> userTransactions=this.transactionRepository.findByUserIdWithDto(userId);

            return new ResponseEntity<>(userTransactions,HttpStatus.OK);
            
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch User Transactions "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

}
