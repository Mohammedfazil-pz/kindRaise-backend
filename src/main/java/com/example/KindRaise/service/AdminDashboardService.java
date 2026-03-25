package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.dtos.CampaignTopRaisedDto;
import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.dtos.UserDashboardChartDto;
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

        Long totalUsers= this.userRepository.countByRole("USER");
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
            Page<DonationDto> donations=this.donationRepository.findAllDonationsByDto(pageable,"");
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

    public ResponseEntity<?> getAdminAnalyticsPageStatsData(){
          Map<String,Object> responseObject=new HashMap<>();
        try {

            Long totalDonations=this.donationRepository.count();
            BigDecimal totalAmountRaised=this.donationRepository.totalAmountRaised();
            Long totalUsers=this.userRepository.countByRole("USER");
            Long totalActiveCampaigns=this.campaignRepository.countByStatusIn(List.of("ACTIVE","COMPLETED"));

            responseObject.put("totalDonations", totalDonations);
            responseObject.put("totalAmountRaised", totalAmountRaised);
            responseObject.put("totalUsers", totalUsers);
            responseObject.put("totalActiveCampaigns", totalActiveCampaigns);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);
            
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch analytics details "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTotalDonationsAnalytics(){
         Map<String,Object> responseObject=new HashMap<>();
        try {
             List<Object[]> userDonations=this.donationRepository.totalDonationsPerMonth();
             List<UserDashboardChartDto> result=userDonations.stream()
            .map((each)->new UserDashboardChartDto((String)each[0],(BigDecimal)each[1])).toList();

           return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch analytics of donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTopThreeRaisedCampaigns(){
         Map<String,Object> responseObject=new HashMap<>();
        try {
            List<CampaignTopRaisedDto> topRaisedCampaigns=this.donationRepository.findTopThreeRaisedCampaigns();
            return new ResponseEntity<>(topRaisedCampaigns,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch Top Three Raised Campaigns "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getThreeRecentDonations() {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            List<DonationDto> topThreeRecentDonations=this.donationRepository.findRecentThreeDonations();
            return new ResponseEntity<>(topThreeRecentDonations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch Top Three Recent donations"+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


      public ResponseEntity<?> dailyDonations(){
        Map<String,Object> responseObject=new HashMap<>();
        try {


           List<Object[]> result = this.donationRepository.dailyDonations();


                Map<String, BigDecimal> map = result.stream()
                    .collect(Collectors.toMap(
                        each -> (String) each[0],
                        each -> (BigDecimal) each[1]
                    ));

                // ✅ Fill last 7 days
                List<UserDashboardChartDto> finalList = new ArrayList<>();

                for (int i = 6; i >= 0; i--) {
                    LocalDate date = LocalDate.now().minusDays(i);
                    String formatted = date.toString();

                    finalList.add(new UserDashboardChartDto(
                        formatted,
                        map.getOrDefault(formatted, BigDecimal.ZERO)
                    ));
                }     

            return new ResponseEntity<>(finalList,HttpStatus.OK);
            
        } catch (Exception e) {
           responseObject.put("message", "Something went wrong While Fetching User Dashbpard"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
