package com.example.KindRaise.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.UserRepository;


import jakarta.transaction.Transactional;

@Service
public class CampaignService {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    Map<String,Object> responseObject=new HashMap<>();


    public ResponseEntity<?> createCampaign(Campaign campaign, MultipartFile file) {
        try {
             String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
            if(!file.isEmpty()){
                campaign.setImageData(file.getBytes());
                campaign.setImageName(file.getOriginalFilename());
                campaign.setImageType(file.getContentType());
            }

            campaign.setStatus("PENDING");

            User userDetails=this.userRepository.findByUsername(username);
            campaign.setUser(userDetails);

            Notification adminNotification=new Notification();

            this.campaignRepository.save(campaign);

            // notification for user
            adminNotification.setTitle("New Campaign Request");
            adminNotification.setMessage("A new campaign was submitted by " + username);
            adminNotification.setCreatedAt(LocalDateTime.now());
            
            this.notificationService.addNotification(adminNotification);
            Notification userNotification=new Notification();

             userNotification.setTitle("Campaign Requested");
             userNotification.setMessage("Your campaign has been requested. please wait for the admin approval");
             userNotification.setUser(userDetails);
             userNotification.setCreatedAt(LocalDateTime.now());

            this.notificationService.addNotification(userNotification);

            responseObject.put("message", "Campaign Created wait for the admin approval");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.CREATED);
    
        } catch (Exception e) {
           responseObject.put("message", "Campaign not created "+e.getMessage());
           responseObject.put("status", false);

         return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

   
    public ResponseEntity<?> getUserCampaigns() {
        try {
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Username"+username);
            User userDetails=this.userRepository.findByUsername(username);
            List<CampaignDto> userCampaigns=this.campaignRepository.findUserCampaigns(userDetails.getId());
            // List<CampaignDto> filteredCampaign=userCampaigns.stream().map(each->new CampaignDto(each)).toList();
            return new ResponseEntity<>(userCampaigns,HttpStatus.OK);
        } catch (Exception e) {
             responseObject.put("message", "Error on fetching campaigns "+e.getMessage());
             responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  
    public ResponseEntity<?> getSingleCampaignById(String id) {
        try {

            CampaignDto campaign=this.campaignRepository.findCampaignDtoById(id).orElse(new CampaignDto());
            return new ResponseEntity<>(campaign,HttpStatus.OK);
        } catch (Exception e) {
             responseObject.put("message", "Error on fetching campaign "+e.getMessage());
             responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getActiveCampaigns() {
        try {
            List<CampaignDto> campaigns=this.campaignRepository.findActiveCampaigns();
            return new ResponseEntity<>(campaigns,HttpStatus.OK);
        } catch (Exception e) {
             responseObject.put("message", "Error on fetching campaigns "+e.getMessage());
             responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getAllCampaigns() {
        try {
            List<CampaignDto> allCampaignDtos=this.campaignRepository.findAllCampaignsWithDto();
            return new ResponseEntity<>(allCampaignDtos,HttpStatus.OK);
        } catch (Exception e) {
             responseObject.put("message", "Error on fetching campaigns "+e.getMessage());
             responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> updateStatusCampaign(String campaignId, String status) {
        try {
            Campaign campaign=this.campaignRepository.findById(campaignId).orElse(null);
            if(campaign==null){
                 responseObject.put("message", "Campaign not found id: "+campaignId);
                 responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }
            campaign.setStatus(status);
            this.campaignRepository.save(campaign);

            responseObject.put("message", "Campaign Status updated Successfully.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Failed while updated the status of Campaign"+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
