package com.example.KindRaise.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationUserStatsDto {
    private BigDecimal totalAmountDonated;
    private Long totalDonations; 
    private Long totalCampaignsDonated;
}
