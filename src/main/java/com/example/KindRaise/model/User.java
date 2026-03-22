package com.example.KindRaise.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String fullName;
     @Column(nullable = false)
    private String status;
    private LocalDate DOB;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;
    private String imageType;
    private String imageName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

