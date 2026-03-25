package com.example.KindRaise.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTopRaisedDto {
    private String id;
    private String title;
    private BigDecimal amount;
    private BigDecimal goalAmount;
}
