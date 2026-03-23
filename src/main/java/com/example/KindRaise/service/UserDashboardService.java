package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.dtos.UserDashboardChartDto;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.DonationRepository;
import com.example.KindRaise.repository.UserRepository;

@Service
public class UserDashboardService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    CampaignRepository campaignRepository;


   
    public ResponseEntity<?> userDashboardStats(){
        Map<String,Object> responseObject=new HashMap<>();
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findUserWithoutImage(username);

            Long totalDonations=this.donationRepository.countByUserId(user.getId());
            DonationDto latestDonation=this.donationRepository.findTopByUserIdOrderByDonationDateDesc(user.getId());
            Long totalActiveCampaigns=this.campaignRepository.countByUserIdAndStatusIs(user.getId(), "ACTIVE");

            responseObject.put("totalDonations", totalDonations);
            responseObject.put("latestDonation", latestDonation);
            responseObject.put("activeCampaigns", totalActiveCampaigns);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);
            
        } catch (Exception e) {
           responseObject.put("message", "Something went wrong While Fetching User Dashbpard"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> userDonationsPerMonth(String userIdFromAdmin){
        Map<String,Object> responseObject=new HashMap<>();
        try {

            String userId;

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findUserWithoutImage(username);

            System.out.println(user.getRole());

            if(user.getRole().equals("USER")){
                userId=user.getId();
            }else{
                userId=userIdFromAdmin;
            }

           List<Object[]> userDonations=this.donationRepository.userDonationsPerMonth(userId);

           List<UserDashboardChartDto> result=userDonations.stream()
           .map((each)->new UserDashboardChartDto((String)each[0],(BigDecimal)each[1])).toList();

            return new ResponseEntity<>(result,HttpStatus.OK);
            
        } catch (Exception e) {
           responseObject.put("message", "Something went wrong While Fetching User Dashbpard"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
