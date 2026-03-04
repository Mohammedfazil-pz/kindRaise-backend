package com.example.KindRaise.dtos;

import java.math.BigDecimal;

import com.example.KindRaise.model.Campaign;

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
    private BigDecimal amount;
    private String transactionStatus;
    private String PaymentReference;
    private String paymentMethod; 
    private String campaignID;
}
