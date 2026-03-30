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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.TransactionDto;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.TransactionRepository;
import com.example.KindRaise.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;


     Map<String,Object> responseObject=new HashMap<>();

     public ResponseEntity<?> getAllTransactions(int page,int size,String campaignId,String status,String search) {
          Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(page,size);
            Page<TransactionDto> donations=this.transactionRepository.findAllTransactionsByDto(search,campaignId,status,pageable);
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch All Transactions "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


        public ResponseEntity<?> getAllTransactionsByOwnerId(int page,int size,String campaignId,String type,String search) {
          Map<String,Object> responseObject=new HashMap<>();
        try {
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findUserWithoutImage(username);
            Pageable pageable=PageRequest.of(page,size);
            Page<TransactionDto> donations=this.transactionRepository.findAllTransactionsByDtoWwithOwnerId(search,campaignId,type,pageable,user.getId());
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch All Transactions "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    


    
     public ResponseEntity<?> getAllTransactionsInOneCall() {
          Map<String,Object> responseObject=new HashMap<>();
        try {
           
            List<TransactionDto> transactions=this.transactionRepository.findAllTransactionsInOneCallByDto();
            return new ResponseEntity<>(transactions,HttpStatus.OK);
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
