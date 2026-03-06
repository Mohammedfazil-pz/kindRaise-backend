package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.example.KindRaise.dtos.RazorPayResponseDto;
import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.PaymentGateway;
import com.example.KindRaise.model.PaymentTransaction;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.PaymentGatewayRepository;
import com.example.KindRaise.repository.TransactionRepository;
import com.example.KindRaise.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

@Service
public class RazorPayService {

    @Autowired
    PaymentGatewayRepository paymentGatewayRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DonationService donationService;

    Map<String,Object> responseObject=new HashMap<>();

    public Map<String,Object> createOrder(BigDecimal amount,String campaignId){

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

        System.out.println(amount);

        options.put("amount", amount.multiply(new BigDecimal(100))); // in paise
        System.out.println(options.get("amount"));
        options.put("currency", "INR");
        options.put("receipt", "txn_" + UUID.randomUUID());

        Order order=razorpayClient.orders.create(options);

        responseObject.put("orderId", order.get("id"));
        responseObject.put("amount", order.get("amount"));
        responseObject.put("key", paymentGateway.getApiKey());

         PaymentTransaction paymentTransaction =new PaymentTransaction();
       

         String username=SecurityContextHolder.getContext().getAuthentication().getName();

         User user= this.userRepository.findByUsername(username);

        Campaign campaign=this.campaignRepository.findById(campaignId).get();


         paymentTransaction.setAmount(amount);;
         paymentTransaction.setTransactionDate(LocalDateTime.now());
        paymentTransaction.setPaymentReference(order.get("id"));
         paymentTransaction.setTransactionStatus("PENDING");
         paymentTransaction.setPaymentMethod("ONLINE");
         paymentTransaction.setCampaign(campaign);
         paymentTransaction.setUser(user);

         PaymentTransaction savedPaymentTransaction=this.transactionRepository.save(paymentTransaction);

         responseObject.put("transaction_id", savedPaymentTransaction.getId());

         return responseObject;
       } catch (Exception e) {
            responseObject.put("message", "Failed to Create order "+e.getMessage());
            responseObject.put("status", false);

            return responseObject;
       }
    }


    public ResponseEntity<?> verifySignature(RazorPayResponseDto razorPayResponseDto) {

    try {
        Optional<PaymentGateway> gatewayOptional = paymentGatewayRepository.findByActiveTrue();

        if (gatewayOptional.isEmpty()) {
            throw new RuntimeException("No active gateway found");
        }

        String secret = gatewayOptional.get().getSecretKey();

        String payload = razorPayResponseDto.getRazorpay_order_id() + "|" + razorPayResponseDto.getRazorpay_payment_id();

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());

        String generatedSignature = new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));

        boolean isValid = generatedSignature.equals(razorPayResponseDto.getRazorpay_signature());

        if (!isValid) {
            throw new RuntimeException("Invalid signature");
        }

       Optional<Campaign> campaign=this.campaignRepository.findById(razorPayResponseDto.getCampaignID());

       
       if(campaign.isEmpty()){
          responseObject.put("message","Campaign not found");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
       }

       String username=SecurityContextHolder.getContext().getAuthentication().getName();

       
       User user= this.userRepository.findByUsername(username);
       Campaign campaignDto=campaign.get();

       if(!campaignDto.getStatus().equals("ACTIVE")){
            responseObject.put("message","Sorry you can't donate. The campaign is not active now.");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

        }

        if(campaignDto.getDeadline().isBefore(LocalDate.now())){
            responseObject.put("message", "Sorry, you can't donate. The campaign deadline has expired.");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);   
            
        }

           PaymentGateway paymentGateway=this.paymentGatewayRepository.findByActiveTrue().get();

           RazorpayClient razorpayClient=new RazorpayClient(paymentGateway.getApiKey(), paymentGateway.getSecretKey());

           Payment payment=razorpayClient.payments.fetch(razorPayResponseDto.getRazorpay_payment_id());

            String method=payment.get("method");

           PaymentTransaction paymentTransaction=this.transactionRepository.findById(razorPayResponseDto.getTransactionId()).get();

           paymentTransaction.setTransactionStatus("SUCCESS");
           paymentTransaction.setPaymentMethod(method);
           paymentTransaction.setPaymentReference(razorPayResponseDto.getRazorpay_payment_id());
           this.donationService.donateCampaign(campaignDto,razorPayResponseDto,user);
            this.transactionRepository.save(paymentTransaction);

            responseObject.put("message", "You donation is successfully added.");
            responseObject.put("status", true);
            return new ResponseEntity<>(responseObject, HttpStatus.CREATED);


    } catch (Exception e) {
        e.printStackTrace();
         throw new RuntimeException("Signature verification failed: " + e.getMessage());
    }
}


    public void failedPayment(RazorPayResponseDto razorPayResponseDto) {
        try {
          
             Optional<Campaign> campaign=this.campaignRepository.findById(razorPayResponseDto.getCampaignID());
              if(campaign.isEmpty()){
                throw new RuntimeException("Campaign not found");
            }

            PaymentTransaction paymentTransaction=this.transactionRepository.findById(razorPayResponseDto.getTransactionId()).get();

            paymentTransaction.setTransactionStatus("FAILED");

            this.transactionRepository.save(paymentTransaction);

        } catch (Exception e) {
             throw new RuntimeException("Server error in failedPaymentController");
        }
    }

}
