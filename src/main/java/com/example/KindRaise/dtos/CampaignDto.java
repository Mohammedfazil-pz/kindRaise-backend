package com.example.KindRaise.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    private String id;
    private String title;
    private String description;
    private BigDecimal goalAmount;
    private LocalDate deadline;
    private String status;
    private String user_id;

    
}
