package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.KindRaise.model.PaymentGateway;

import jakarta.transaction.Transactional;

public interface PaymentGatewayRepository extends JpaRepository<PaymentGateway,String> {

    @Transactional
    @Modifying
    @Query("UPDATE PaymentGateway c set c.active=false")
    void deactivateAllOtherGateway();

}
