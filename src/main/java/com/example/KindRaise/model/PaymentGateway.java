package com.example.KindRaise.model;

import java.time.LocalDateTime;

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
    @Column(nullable = false)
    private String name;
     @Column(nullable = false)
    private String apiKey;
     @Column(nullable = false)
    private String secretKey;
     @Column(nullable = false)
    private String mode;
     @Column(nullable = false)
    private Boolean active;
    private LocalDateTime createdAt;
}
