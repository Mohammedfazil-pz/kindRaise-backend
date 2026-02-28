package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.model.PaymentTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransaction,String>{

}
