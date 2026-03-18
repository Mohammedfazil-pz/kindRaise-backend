package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.DonationRepository;
import com.example.KindRaise.repository.UserRepository;

@Service
public class AdminDashboardService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    DonationRepository donationRepository;

    public ResponseEntity<?> adminDashboardStatsDetails() {
        Map<String,Object> responseObject=new HashMap<>();
       try {

        Long totalUsers= this.userRepository.count();
        Long totalActiveCampaigns=this.campaignRepository.countByStatus("ACTIVE");
        Long totalDonations=this.donationRepository.count();
        BigDecimal totalAmountRaised=this.donationRepository.totalAmountRaised();

        responseObject.put("totalUsers", totalUsers);
        responseObject.put("totalActiveCampaign", totalActiveCampaigns);
        responseObject.put("totalDonations",totalDonations);
        responseObject.put("totalAmountRaised", totalAmountRaised);

        return new ResponseEntity<>(responseObject,HttpStatus.OK);

        
       } catch (Exception e) {
            responseObject.put("message", "Failed to fetch dashboard stats "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    public ResponseEntity<?> getRecentCampaigns() {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(0, 3);
            List<CampaignDto> campaigns=this.campaignRepository.findAllCampaignsWithDto(pageable);
            return new ResponseEntity<>(campaigns,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch Recent Campaigns "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getRecentDonations() {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(0, 3);
            List<DonationDto> donations=this.donationRepository.findAllDonationsByDto(pageable);
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch Recent Donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getRecentUsers() {
       Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(0, 3);
            List<User> users=this.userRepository.findAllUserWithoutImage(pageable);
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch Recent Users "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
