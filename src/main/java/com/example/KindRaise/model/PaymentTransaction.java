package com.example.KindRaise.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String transactionStatus;
    @Column(nullable = false)
    private String PaymentReference;
    @Column(nullable = false)
    private String paymentMethod; 
   

     private LocalDateTime transactionDate;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     @ManyToOne
     @JoinColumn(name = "campaign_id")
     private Campaign campaign;

     @ManyToOne
     @JoinColumn(name = "payment_gatway_id")
     private PaymentGateway paymentGateway;



}
