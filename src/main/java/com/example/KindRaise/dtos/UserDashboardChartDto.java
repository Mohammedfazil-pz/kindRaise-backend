package com.example.KindRaise.dtos;



import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboardChartDto {
    private String month;
    private BigDecimal totalAmount;
}
