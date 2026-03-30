package com.example.KindRaise.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDonationDto {
    private String title;
    private String description;
    private BigDecimal amount; 
    private BigDecimal totalAmount;
    private BigDecimal goalAmount;
    private String campaign_id;
    private LocalDate deadline;
   
}
