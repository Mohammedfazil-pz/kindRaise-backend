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
public class Ticket {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Boolean readStatus=false;
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
