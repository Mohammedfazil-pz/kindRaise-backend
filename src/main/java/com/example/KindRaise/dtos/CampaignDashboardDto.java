package com.example.KindRaise.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDashboardDto {
    private Long totalCampaigns;
    private BigDecimal totalAmount;
}
