package com.example.KindRaise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.TransactionDto;
import com.example.KindRaise.model.PaymentTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransaction,String>{

    @Query("""
            SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName
            ) FROM PaymentTransaction t
            """)
    public List<TransactionDto> findAllTransactionsByDto();
}
