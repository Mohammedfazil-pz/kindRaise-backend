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
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDashboardStatsDetails(){
        return this.adminDashboardService.adminDashboardStatsDetails();
    }

    @GetMapping("/recent/campaigns")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRecentCampaigns(){
        return this.adminDashboardService.getRecentCampaigns();
    }

    @GetMapping("/recent/donations")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRecentDonations(){
        return this.adminDashboardService.getRecentDonations();
    }

    @GetMapping("/recent/users")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRecentUsers(){
        return this.adminDashboardService.getRecentUsers();
    }

    @GetMapping("/analytics/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminAnalyticsPageStatsData(){
        return this.adminDashboardService.getAdminAnalyticsPageStatsData();
    }

    @GetMapping("/analytics/donations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTotalDonationsAnalytics(){
        return this.adminDashboardService.getTotalDonationsAnalytics();
    }
    
    @GetMapping("/analytics/top/raised/campaigns")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTopThreeRaisedCampaigns(){
        return this.adminDashboardService.getTopThreeRaisedCampaigns();
    }

    @GetMapping("/analytics/recent/donations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getThreeRecentDonations(){
        return this.adminDashboardService.getThreeRecentDonations();
    }

    @GetMapping("/dailydonations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> dailyDonations(){
        return this.adminDashboardService.dailyDonations();
    }
    
}
