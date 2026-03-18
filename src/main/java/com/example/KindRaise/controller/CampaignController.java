package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.dtos.CampaignRequestDto;

import com.example.KindRaise.service.CampaignService;



@CrossOrigin
@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    @Autowired
    CampaignService campaignService;
     
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> createCampaign(@RequestPart("campaign") CampaignRequestDto campaign,@RequestPart("file") MultipartFile file){
        return this.campaignService.createCampaign(campaign,file);
    }

    @GetMapping("/user/campaigns")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserCampaigns(){
        return this.campaignService.getUserCampaigns();
    }
    
    @GetMapping("/user/campaign/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getSingleCampaignById(@PathVariable("id") String id){
        return this.campaignService.getSingleCampaignById(id);
    }

    @PutMapping("/update/campaign/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> updateCampaign(@PathVariable("id") String id,@RequestPart("campaign") CampaignRequestDto campaign,@RequestPart(value = "file", required = false) MultipartFile file ){
        return this.campaignService.updateCampaign(id, campaign, file);
    }

    @DeleteMapping("/delete/campaign/{id}")
     @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> deleteCampaign(@PathVariable("id") String id){
        return this.campaignService.deleteCampaign(id);
    }


    @GetMapping("/active/campaigns")
    public ResponseEntity<?> getActiveCampaigns(
    @RequestParam(value = "search", required = false) String searchQuery,
    @RequestParam(value = "category", required = false) String category,
    @RequestParam(value = "status",required = false) String status
){
        
        return this.campaignService.getActiveCampaigns(searchQuery,category,status);
    }

    
    @GetMapping("/all/campaigns")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCampaigns(){
        return this.campaignService.getAllCampaigns();
    }

    @PutMapping("/update/campaign/status/{campaignID}/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> updateStatusCampaign(@PathVariable("campaignID") String campaignId,@PathVariable("status") String status){
        return this.campaignService.updateStatusCampaign(campaignId,status);
    }

    @GetMapping("/image/campaign/{id}")
    public ResponseEntity<?> getCampaignImage(@PathVariable("id") String id){
        return this.campaignService.getCampaignImage(id);
    }

    @GetMapping("/dashboard/user")
    public ResponseEntity<?> userCampaignDashboard(){
        return this.campaignService.userCampaignDashboard();
    }

   

    

   

}
