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
    private String id;
    private BigDecimal amount;
    private LocalDate donationDate;
    private String transactionId;
    private String title;
    private String campaign_id;
}
