package com.example.KindRaise.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTotalDonationDto {
    private String campaign_id;
    private BigDecimal amount;
}
