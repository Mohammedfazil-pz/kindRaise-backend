package com.example.KindRaise.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.UserRepository;
import com.example.KindRaise.utils.CampaignUtil;

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

    // @Transactional
    // public ResponseEntity<?> getUserCampaigns() {
    //     try {
    //         String username=SecurityContextHolder.getContext().getAuthentication().getName();
    //         System.out.println("Username"+username);
    //         User userDetails=this.userRepository.findByUsername(username);
    //         List<Campaign> userCampaigns=this.campaignRepository.findByUserId(userDetails.getId());
    //         System.out.println(userCampaigns);
    //         return new ResponseEntity<>(userDetails,HttpStatus.OK);
    //     } catch (Exception e) {
    //          responseObject.put("message", "Error on fetching campaigns "+e.getMessage());
    //          responseObject.put("status", false);

    //         return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}
