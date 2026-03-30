package com.example.KindRaise.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.TransactionDto;
import com.example.KindRaise.model.PaymentTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransaction,String>{

    @Query("""
            SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName,t.paymentMethod
            ) FROM PaymentTransaction t WHERE 
             (COALESCE(:search,'')='' OR LOWER(t.user.fullName) LIKE LOWER(CONCAT('%',:search,'%')) OR
             LOWER(t.campaign.title) LIKE LOWER(CONCAT('%',:search,'%'))
             )
             AND (COALESCE(:campaignId,'')='' OR t.campaign.id=:campaignId) AND
             (COALESCE(:status,'')='' OR t.transactionStatus=:status) ORDER BY transactionDate DESC
            """)
     Page<TransactionDto> findAllTransactionsByDto(String search,String campaignId,String status,Pageable page);


     @Query("""
            SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName,t.paymentMethod
            ) FROM PaymentTransaction t WHERE 
             (
                (:type='INCOME' AND t.campaign.user.id=:ownerId AND t.transactionStatus='SUCCESS')
                OR
                (:type='OUTCOME' AND t.user.id=:ownerId AND t.transactionStatus='SUCCESS')
                OR
                (
                 (:type IS NULL OR :type = '')
                 AND
                (t.user.id = :ownerId OR t.campaign.user.id = :ownerId)
                )
             ) AND
           
             (COALESCE(:search,'')='' OR LOWER(t.user.fullName) LIKE LOWER(CONCAT('%',:search,'%')) OR
             LOWER(t.campaign.title) LIKE LOWER(CONCAT('%',:search,'%'))
             )
             AND (COALESCE(:campaignId,'')='' OR t.campaign.id=:campaignId) ORDER BY transactionDate DESC
            """)
     Page<TransactionDto> findAllTransactionsByDtoWwithOwnerId(String search,String campaignId,String type,Pageable page,String ownerId);


    @Query("""
            SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName,t.paymentMethod
            ) FROM PaymentTransaction t  ORDER BY t.transactionDate DESC
            """)
     List<TransactionDto> findAllTransactionsInOneCallByDto();

    @Query("""
             SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName,t.paymentMethod
            ) FROM PaymentTransaction t WHERE t.user.id=:userId ORDER BY t.transactionDate DESC
            """)
    Page<TransactionDto> findUserTransactionsWithDto(String userId,Pageable page);

    @Query("""
         SELECT new com.example.KindRaise.dtos.TransactionDto(
            t.id,t.amount,t.transactionStatus,t.PaymentReference,t.transactionDate,t.campaign.id,t.campaign.title,t.user.id,t.user.fullName,t.paymentMethod
            ) FROM PaymentTransaction t WHERE t.user.id=:userId ORDER BY t.transactionDate DESC
    """)
    List<TransactionDto> findByUserIdWithDto(String userId);
}
