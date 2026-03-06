package com.example.KindRaise.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.dtos.CampaignImageDto;
import com.example.KindRaise.dtos.CampaignRequestDto;
import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.Category;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.CategoryRepository;
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

    @Autowired
    CategoryRepository categoryRepository;

    Map<String,Object> responseObject=new HashMap<>();


    public ResponseEntity<?> createCampaign(CampaignRequestDto campaignRequestDto, MultipartFile file) {
        try {
            Campaign campaign = new Campaign();

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

            Category isCategoryExists= this.categoryRepository.findById(campaignRequestDto.getCategoryId()).get();

            campaign.setTitle(campaignRequestDto.getTitle());
            campaign.setGoalAmount(campaignRequestDto.getGoalAmount());
            campaign.setDescription(campaignRequestDto.getDescription());
            campaign.setDeadline(campaignRequestDto.getDeadline());
            campaign.setCategory(isCategoryExists);

           

            this.campaignRepository.save(campaign);

             Notification adminNotification=new Notification();

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
            Optional<Campaign> checkingCampaign=this.campaignRepository.findById(campaignId);
            if(checkingCampaign.isEmpty()){
                 responseObject.put("message", "Campaign not found id: "+campaignId);
                 responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }
            
            Campaign campaign=checkingCampaign.get();

            campaign.setStatus(status);
            this.campaignRepository.save(campaign);

            Notification userNotification=new Notification();

            userNotification.setTitle("Campaign " + status);
            userNotification.setMessage("Your campaign '" + campaign.getTitle() + "' has been " + status + ".");
            userNotification.setUser(campaign.getUser());
            userNotification.setCreatedAt(LocalDateTime.now());

            responseObject.put("message", "Campaign Status updated Successfully!");
            responseObject.put("status", true);

            this.notificationService.addNotification(userNotification);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Failed while updated the status of Campaign"+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> getCampaignImage(String id) {
        try {
            Optional<CampaignImageDto> imageChecking=this.campaignRepository.getCampaignImage(id);
            if(imageChecking.isEmpty()){
                responseObject.put("message", "image not found");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            } 

            CampaignImageDto imageData=imageChecking.get();
             
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageData.getImageType())).body(imageData.getImageData());

        } catch (Exception e) {
            responseObject.put("message", "Failed while fetching campaign image "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateCampaign(String id,CampaignRequestDto campaign,MultipartFile file){
        try {

            Optional<Campaign> isCampaignExists=this.campaignRepository.findById(id);
            if(isCampaignExists.isEmpty()){
                responseObject.put("message", "Campaign not found");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
            }

            Campaign campaignData=isCampaignExists.get();

            campaignData.setTitle(campaign.getTitle());
            campaignData.setDescription(campaign.getDescription());
            campaignData.setGoalAmount(campaign.getGoalAmount());
            campaignData.setDeadline(campaign.getDeadline());


            Category isCategoryExists= this.categoryRepository.findById(campaign.getCategoryId()).get();

            campaignData.setCategory(isCategoryExists);

            if(file!=null && !file.isEmpty()){
                campaignData.setImageData(file.getBytes());
                campaignData.setImageName(file.getOriginalFilename());
                campaignData.setImageType(file.getContentType());
            }

            this.campaignRepository.save(campaignData);

            responseObject.put("message", "Campaign updated successfully!");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        
        } catch (Exception e) {
            responseObject.put("message", "Failed to update campaign "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteCampaign(String id){
        try {
            this.campaignRepository.deleteById(id);
             responseObject.put("message", "Campaign Removed successfully!");
             responseObject.put("status", true);

            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to Delete campaign "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
