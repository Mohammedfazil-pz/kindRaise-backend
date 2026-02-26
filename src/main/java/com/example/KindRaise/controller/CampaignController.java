package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.service.CampaignService;

@CrossOrigin
@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    @Autowired
    CampaignService campaignService;
     
    @PostMapping("/create")
    public ResponseEntity<?> createCampaign(@RequestPart("campaign") Campaign campaign,@RequestPart MultipartFile file){
        return this.campaignService.createCampaign(campaign,file);
    }

    @GetMapping("/user/campaigns")
    public ResponseEntity<?> getUserCampaigns(){
        return this.campaignService.getUserCampaigns();
    }
}
