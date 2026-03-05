package com.example.KindRaise.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String id;
    private BigDecimal amount;
    private String transactionStatus;
    private String PaymentReference;
    private LocalDateTime transactionDate;
    private String campaign_id;
    private String campaign_name;
    private String user_id;
    private String user_name;
}
