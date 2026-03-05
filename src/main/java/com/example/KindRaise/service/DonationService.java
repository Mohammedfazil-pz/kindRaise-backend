package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.dtos.RazorPayResponseDto;
import com.example.KindRaise.dtos.UserDonationDto;
import com.example.KindRaise.model.Campaign;
// import com.example.KindRaise.model.Campaign;
import com.example.KindRaise.model.Donation;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.PaymentTransaction;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.DonationRepository;
import com.example.KindRaise.repository.TransactionRepository;
import com.example.KindRaise.repository.UserRepository;

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

    @Autowired
    UserRepository userRepository;

    Map<String,Object> responseObject=new HashMap<>();

    @Transactional
    public ResponseEntity<?> donateCampaign(Campaign campaignDto, RazorPayResponseDto razorPayResponseDto,User user) {
       try {

        Donation donation = new Donation();

        User campaignOwner=this.userRepository.findById(campaignDto.getUser().getId()).get();

        donation.setAmount(razorPayResponseDto.getAmount());
        donation.setCampaign(campaignDto);
        donation.setDonationDate(LocalDate.now());
        donation.setTransactionId(razorPayResponseDto.getRazorpay_payment_id());
        donation.setUser(user);
      
        Donation saveDonation= this.donationRepository.save(donation);

         Notification adminNotification=new Notification();
         Notification campaignUserNotification=new Notification();
         Notification donorNotification=new Notification();

         adminNotification.setTitle("New Donation Received");
         adminNotification.setMessage(
                user.getUsername() + " donated ₹" + razorPayResponseDto.getAmount() +
                " to campaign " + campaignDto.getTitle()
        );
        adminNotification.setCreatedAt(LocalDateTime.now());
        adminNotification.setUser(null);


        campaignUserNotification.setTitle("Your Campaign Received a Donation");
        campaignUserNotification.setMessage(
                user.getUsername() + " donated ₹" + razorPayResponseDto.getAmount() +
                " to your campaign \"" + campaignDto.getTitle() + "\""
        );
        campaignUserNotification.setCreatedAt(LocalDateTime.now());
        campaignUserNotification.setUser(campaignOwner);


        donorNotification.setTitle("Donation Successful");
        donorNotification.setMessage(
                "Thank you for donating ₹" + razorPayResponseDto.getAmount() +
                " to campaign \"" + campaignDto.getTitle() + "\""
        );
        donorNotification.setCreatedAt(LocalDateTime.now());
        donorNotification.setUser(user);

        this.notificationService.addNotification(adminNotification);
        this.notificationService.addNotification(campaignUserNotification);
        this.notificationService.addNotification(donorNotification);
        

        return new ResponseEntity<>(saveDonation,HttpStatus.OK);


        
       } catch (Exception e) {
            responseObject.put("message", "Failed to Donate to this campaign "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    public ResponseEntity<?> getDonationsOfCampaign(String id){
        try {

         List<DonationDto> donations=this.donationRepository.findByCampaignIdByDto(id);
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch campaign donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUserDonations(){
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User userDetails=this.userRepository.findByUsername(username);
            List<UserDonationDto> donations=this.donationRepository.findDonationByUserId(userDetails.getId());
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch User donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getAllDonations() {
        try {
            List<DonationDto> donations=this.donationRepository.findAllDonationsByDto();
            return new ResponseEntity<>(donations,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to fetch All donations "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
