package com.example.KindRaise.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testimonial {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String name;
    private String role;
    private String company;
    private String message;
    private Integer rating;
    private Boolean status;
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;
    private String imageName;
    private String imageType;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
