package com.example.KindRaise.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.Donation;
import com.example.KindRaise.model.PaymentTransaction;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.DonationRepository;
import com.example.KindRaise.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class DonationService {

    @Autowired
    NotificationService notificationService;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    TransactionRepository transactionRepository;

    Map<String,Object> responseObject=new HashMap<>();

    @Transactional
    public ResponseEntity<?> donateCampaign(String id, Donation donation) {
       try {

        Optional<CampaignDto> isCampaignExists=this.campaignRepository.findCampaignDtoById(id);

        if(isCampaignExists.isEmpty()){
            responseObject.put("message", "Campaign not found");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
        }

        CampaignDto campaignDetails=isCampaignExists.get();

        if(campaignDetails.getStatus()!="ACTIVE"){
            responseObject.put("message","Sorry you can't donate. The campaign is not active now.");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);

        }

        if(campaignDetails.getDeadline().isBefore(LocalDate.now())){
            responseObject.put("message", "Sorry, you can't donate. The campaign deadline has expired.");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);   
        }

        PaymentTransaction paymentTransaction = new PaymentTransaction();

        this.donationRepository.save(donation);

        return new ResponseEntity<>(paymentTransaction,HttpStatus.OK);


        
       } catch (Exception e) {
            responseObject.put("message", "Failed to Delete campaign "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
