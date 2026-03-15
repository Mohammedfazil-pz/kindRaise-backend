package com.example.KindRaise.model;

import java.math.BigDecimal;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String title;
     @Column(nullable = false,columnDefinition = "TEXT")
    private String description;
     @Column(nullable = false)
    private BigDecimal goalAmount;
     @Column(nullable = false)
    private LocalDate deadline;
    private String status;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;
    private String imageName;
    private String imageType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


}
