package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.model.Donation;
import com.example.KindRaise.service.DonationService;

@CrossOrigin
@RestController
@RequestMapping("/api/donation")
public class DonationController {

    @Autowired
    DonationService donationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDonationsOfCampaign(@PathVariable("id") String id){
        System.out.println(id);
        return this.donationService.getDonationsOfCampaign(id);
    }
}
