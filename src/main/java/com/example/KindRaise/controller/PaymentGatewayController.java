package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.model.PaymentGateway;
import com.example.KindRaise.repository.PaymentGatewayRepository;
import com.example.KindRaise.service.PaymentGatewayService;

@RestController
@RequestMapping("/api/gateway")
public class PaymentGatewayController{

    

    @Autowired
    PaymentGatewayService paymentGatewayService;

  
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addGateway(@RequestBody PaymentGateway paymentGateway){
        return this.paymentGatewayService.addGateway(paymentGateway);
    }
    
    @PutMapping("/active/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enableGateway(@PathVariable("id") String id){
        return this.paymentGatewayService.enableGateway(id);
    }

    @GetMapping("/fetch/gateways")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchGateways(){
        return this.paymentGatewayService.fetchGateways();
    }

    @PutMapping("/update/gateway/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGateway(@PathVariable("id") String id,@RequestBody PaymentGateway paymentGateway){
        return this.paymentGatewayService.updateGateway(id,paymentGateway);
    }

    @DeleteMapping("/delete/gateway/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGateway(@PathVariable("id") String id){
        System.out.println(id);
        return this.paymentGatewayService.deleteGateway(id);
    }
    



}
