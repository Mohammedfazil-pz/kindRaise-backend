package com.example.KindRaise.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.PaymentGateway;
import com.example.KindRaise.repository.PaymentGatewayRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentGatewayService {

    @Autowired
    PaymentGatewayRepository paymentGatewayRepository;

     Map<String,Object> responseObject=new HashMap<>();

    public ResponseEntity<?> addGateway(PaymentGateway paymentGateway) {
        try {
  
            this.paymentGatewayRepository.save(paymentGateway);
            Notification adminNotification=new Notification();

            adminNotification.setTitle("New Gateway Added");
            adminNotification.setMessage("A new Gateway was Added " + paymentGateway.getName());
            adminNotification.setCreatedAt(LocalDateTime.now());

            responseObject.put("message", "Gateway added successfully.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.CREATED);

        } catch (Exception e) {
            responseObject.put("message", "Failed to Add Gateway "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> enableGateway(String id){
        try {
            Optional<PaymentGateway> isPaymentGatewayExist=this.paymentGatewayRepository.findById(id);

            if(isPaymentGatewayExist.isEmpty()){
                 responseObject.put("message", "Gateway not found ");
                 responseObject.put("status", false);
                 return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            this.paymentGatewayRepository.deactivateAllOtherGateway();

            PaymentGateway paymentGateway=isPaymentGatewayExist.get();

            paymentGateway.setActive(true);

            this.paymentGatewayRepository.save(paymentGateway);

            responseObject.put("message", "Enabled Gateway.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);



        } catch (Exception e) {
            responseObject.put("message", "Failed on Activating a Gateway "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> fetchGateways() {
            try {
                List<PaymentGateway> paymentGateways=this.paymentGatewayRepository.findAll();
                  return new ResponseEntity<>(paymentGateways,HttpStatus.OK);

            } catch (Exception e) {
                  responseObject.put("message", "Failed on Fetching a Gateway "+e.getMessage());
                  responseObject.put("status", false);

                  return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity<?> updateGateway(String id,PaymentGateway paymentGateway) {
        try {
            Optional<PaymentGateway> isPaymentGatewayExist=this.paymentGatewayRepository.findById(id);

            if(isPaymentGatewayExist.isEmpty()){
                 responseObject.put("message", "Gateway not found ");
                 responseObject.put("status", false);
                 return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            PaymentGateway paymentGatewayData=isPaymentGatewayExist.get();

            paymentGatewayData.setApiKey(paymentGateway.getApiKey());
            paymentGatewayData.setSecretKey(paymentGateway.getSecretKey());
            paymentGatewayData.setMode(paymentGateway.getMode());
            paymentGatewayData.setName(paymentGateway.getName());
          

            this.paymentGatewayRepository.save(paymentGatewayData);

            responseObject.put("message", "Gateway updated.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);


        } catch (Exception e) {
            responseObject.put("message", "Failed on Updating a Gateway "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteGateway(String id) {
         try {
             Optional<PaymentGateway> isPaymentGatewayExist=this.paymentGatewayRepository.findById(id);

            if(isPaymentGatewayExist.isEmpty()){
                 responseObject.put("message", "Gateway not found ");
                 responseObject.put("status", false);
                 return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            this.paymentGatewayRepository.deleteById(id);

            responseObject.put("message", "Gateway Deleted.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);


        } catch (Exception e) {
            responseObject.put("message", "Failed on Deleting a Gateway "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
