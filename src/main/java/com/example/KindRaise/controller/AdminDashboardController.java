package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    AdminDashboardService adminDashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<?> adminDashboardStatsDetails(){
        return this.adminDashboardService.adminDashboardStatsDetails();
    }

    @GetMapping("/recent/campaigns")
    public ResponseEntity<?> getRecentCampaigns(){
        return this.adminDashboardService.getRecentCampaigns();
    }

    @GetMapping("/recent/donations")
    public ResponseEntity<?> getRecentDonations(){
        return this.adminDashboardService.getRecentDonations();
    }

    @GetMapping("/recent/users")
    public ResponseEntity<?> getRecentUsers(){
        return this.adminDashboardService.getRecentUsers();
    }
    
}
