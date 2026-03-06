package com.example.KindRaise.dtos;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorPayResponseDto {
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;
    private String transactionId;
    private String campaignID;
    private BigDecimal amount;
}
