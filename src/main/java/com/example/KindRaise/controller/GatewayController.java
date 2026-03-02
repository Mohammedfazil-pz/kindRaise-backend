package com.example.KindRaise.controller;




import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.dtos.PaymentRequestDto;
import com.example.KindRaise.service.RazorPayService;




@CrossOrigin
@RestController
@RequestMapping("/api/razorpay")
public class GatewayController {

    @Autowired
    RazorPayService razorPayService;

@PostMapping("/create")
public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDto paymentRequestDto) throws Exception {

    Map<String, Object> response = this.razorPayService.createOrder(paymentRequestDto.getAmount());

    return ResponseEntity.ok(response);
}

@PostMapping("/verify")
public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) {

    String orderId = request.get("razorpay_order_id");
    String paymentId = request.get("razorpay_payment_id");
    String signature = request.get("razorpay_signature");

    boolean isValid = this.razorPayService.verifySignature(orderId, paymentId, signature);

    if (isValid) {
        return ResponseEntity.ok("Payment Verified Successfully");
    } else {
        return ResponseEntity.badRequest().body("Invalid Signature");
    }
}
  
}
