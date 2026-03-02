package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.model.PaymentGateway;
import com.example.KindRaise.repository.PaymentGatewayRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class RazorPayService {

    @Autowired
    PaymentGatewayRepository paymentGatewayRepository;

    Map<String,Object> responseObject=new HashMap<>();

    public Map<String,Object> createOrder(BigDecimal amount){

       try {
         Optional<PaymentGateway> isPaymentGatewayExists=this.paymentGatewayRepository.findByActiveTrue();

        if(isPaymentGatewayExists.isEmpty()){
            responseObject.put("message", "No active gateway's found.");
            responseObject.put("status", false);
            return responseObject;
        }

        PaymentGateway paymentGateway=isPaymentGatewayExists.get();

        RazorpayClient razorpayClient=new RazorpayClient(paymentGateway.getApiKey(), paymentGateway.getSecretKey());

        JSONObject options=new JSONObject();

        options.put("amount", amount.multiply(new BigDecimal(100))); // in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + UUID.randomUUID());

        Order order=razorpayClient.orders.create(options);

        responseObject.put("orderId", order.get("id"));
        responseObject.put("amount", order.get("amount"));
        responseObject.put("key", paymentGateway.getApiKey());

         return responseObject;
       } catch (Exception e) {
            responseObject.put("message", "Failed to Create order "+e.getMessage());
            responseObject.put("status", false);

            return responseObject;
       }
    }


    public boolean verifySignature(String orderId, String paymentId, String signature) {

    try {
        Optional<PaymentGateway> gatewayOptional = paymentGatewayRepository.findByActiveTrue();

        if (gatewayOptional.isEmpty()) {
            throw new RuntimeException("No active gateway found");
        }

        String secret = gatewayOptional.get().getSecretKey();

        String payload = orderId + "|" + paymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());

        String generatedSignature = new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));

        return generatedSignature.equals(signature);

    } catch (Exception e) {
        throw new RuntimeException("Signature verification failed");
    }
}

}
