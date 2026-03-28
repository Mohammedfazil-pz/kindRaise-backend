package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.KindRaise.service.DonationService;

@CrossOrigin
@RestController
@RequestMapping("/api/donation")
public class DonationController {

    @Autowired
    DonationService donationService;
    

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getDonationsOfCampaign(@PathVariable("id") String id){
        System.out.println(id);
        return this.donationService.getDonationsOfCampaign(id);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getUserDonations(){
        return this.donationService.getUserDonations();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDonations(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "7") int size,
    @RequestParam(defaultValue = "") String search
){
        return this.donationService.getAllDonations(page,size,search);
    }

    @GetMapping("/dashboard/user/stats")
    public ResponseEntity<?> getDashboardStats(){
        return this.donationService.getDashboardStats();
    }

    @GetMapping("/admin/dashboard/donation/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminDashboardDonationStats(){
        return this.donationService.getAdminDashboardDonationStats();
    }

    @GetMapping("/admin/all/once")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchAllDonations(){
        return this.donationService.fetchAllDonations();
    }
}
