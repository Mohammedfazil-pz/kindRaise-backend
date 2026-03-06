package com.example.KindRaise.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationDto {
    private String id;
    private String title;
    private BigDecimal amount;
    private LocalDate donationDate;
    private String transactionId;
    private String user_id;
    private String fullName;

}
