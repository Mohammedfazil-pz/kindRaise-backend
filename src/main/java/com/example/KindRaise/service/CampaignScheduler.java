package com.example.KindRaise.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.KindRaise.repository.CampaignRepository;

import jakarta.transaction.Transactional;

@Service
public class CampaignScheduler {

    @Autowired
    CampaignRepository campaignRepository;

   @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void updateCampaignStatus(){
        campaignRepository.markCompleted();
        campaignRepository.markExpired(LocalDate.now());
        System.out.println("Campaign status updated");
    }
    
}
