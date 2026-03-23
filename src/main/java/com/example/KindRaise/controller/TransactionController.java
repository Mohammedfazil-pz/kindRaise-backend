package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTransactions(){
        return this.transactionService.getAllTransactions();
    }
    @GetMapping("/user/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserAllTransactionsInOneCall(@PathVariable String id){
        return this.transactionService.getUserAllTransactionsInOneCall(id);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserTransactions(@PathVariable String id,@RequestParam(defaultValue = "0") int page
    ,@RequestParam(defaultValue = "3") int size
    
    ){
        return this.transactionService.getUserTransactions(id, page,size);
    }

}
