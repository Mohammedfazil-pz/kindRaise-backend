package com.example.KindRaise.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.dtos.PaymentRequestDto;
import com.example.KindRaise.dtos.RazorPayResponseDto;
import com.example.KindRaise.service.RazorPayService;





@RestController
@RequestMapping("/api/razorpay")
public class GatewayController {

    @Autowired
    RazorPayService razorPayService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDto paymentRequestDto) throws Exception {
        Map<String, Object> response = this.razorPayService.createOrder(paymentRequestDto.getAmount(),
        paymentRequestDto.getCampaignId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody RazorPayResponseDto razorPayResponseDto) {
        return this.razorPayService.verifySignature(razorPayResponseDto);
    }

    @PostMapping("/payment-failed")
    public void failedPayment(@RequestBody RazorPayResponseDto razorPayResponseDto) {
    this.razorPayService.failedPayment(razorPayResponseDto);
    }

    @GetMapping("/validate-credentials")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> validateRazorpayCredentials() {
        return this.razorPayService.validateRazorpayCredentials();
    }

  
}
