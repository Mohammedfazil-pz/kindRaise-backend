package com.example.KindRaise.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGateway {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
 
    private String name;
    
    private String apiKey;
    
    private String secretKey;

    private String mode;
    
    private Boolean active;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
