package com.example.KindRaise.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.dtos.UserProfileDto;
import com.example.KindRaise.dtos.UserProfileImageDto;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.CampaignRepository;
import com.example.KindRaise.repository.DonationRepository;
import com.example.KindRaise.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    NotificationService notificationService;

    @Autowired
    DonationRepository donationRepository;

    @Autowired
    CampaignRepository campaignRepository;


    public ResponseEntity<?> fetchProfile(){
        Map<String,Object> responseObject=new HashMap<>();

        try {
            String username=SecurityContextHolder.getContext().getAuthentication().getName();

            System.out.println(username);

            if(username.isEmpty()){
                responseObject.put("message", "Please login to fecth profile data");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.UNAUTHORIZED);
            }

            User user=this.userRepository.findUserWithoutImage(username);

            

            return new ResponseEntity<>(user,HttpStatus.OK);


        } catch (Exception e) {
           responseObject.put("message", "Something went wrong"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Transactional
    public ResponseEntity<?> fetchProfileImage(String id){
         Map<String,Object> responseObject=new HashMap<>();

        try {
            String username=SecurityContextHolder.getContext().getAuthentication().getName();

            if(username.isEmpty()){
                responseObject.put("message", "Please login to fetch profile data");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.UNAUTHORIZED);
            }

            UserProfileImageDto user=this.userRepository.findUserProfileImageWithDto(id);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(user.getImageType())).body(user.getImageData());

        } catch (Exception e) {
           responseObject.put("message", "Something went wrong"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> fetchProfileById(String id) {
       Map<String,Object> responseObject=new HashMap<>();

        try {
            User user=this.userRepository.findUserByIdWithoutImage(id);
            Long totalDonations=this.donationRepository.countByUserId(id);
            Long totalCampaignsActive=this.campaignRepository.countByUserIdAndStatus(id,"ACTIVE");
            BigDecimal totalAmountDonated=this.donationRepository.totalAmountUserRaised(id);
            Long totalCampaignsDonated=this.donationRepository.totalCampaignsSupported(id);

            responseObject.put("profile", user);
            responseObject.put("totalDonations", totalDonations);
            responseObject.put("totalCampaignsActive", totalCampaignsActive);
            responseObject.put("totalAmountDonated", totalAmountDonated);
            responseObject.put("totalCampaignsDonated", totalCampaignsDonated);
            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
           responseObject.put("message", "Something went wrong"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<?> updateProfile(String id,User userData,MultipartFile file){
            Map<String,Object> responseObject=new HashMap<>();
        try {

            Optional<User> isUserExists=this.userRepository.findById(id);

            if(isUserExists.isEmpty()){
                responseObject.put("message", "User not found.");
                responseObject.put("status", false);

                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            User user=isUserExists.get();

              if (file != null && !file.isEmpty()) {
                    user.setImageData(file.getBytes());
                    user.setImageName(file.getOriginalFilename());
                    user.setImageType(file.getContentType());
            }
        

            user.setFullName(userData.getFullName());
            user.setPhone(userData.getPhone());
            user.setUsername(userData.getUsername());

            this.userRepository.save(user);
            
            responseObject.put("message", "Profile updated");
            responseObject.put("status", true);

           return new ResponseEntity<>(responseObject,HttpStatus.OK);
            
        } catch (Exception e) {
           responseObject.put("message", "Something went wrong"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> checkPassword(String id,String password ){
            Map<String,Object> responseObject=new HashMap<>();
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();

            User user=this.userRepository.findUserWithoutImage(username);

            System.out.println(user.getPassword());

            boolean isValid= this.bCryptPasswordEncoder.matches(password,user.getPassword());

            if(isValid){
                responseObject.put("message","Password Correct");
                responseObject.put("status",true);
                return new ResponseEntity<>(responseObject,HttpStatus.OK);
            }else{
                responseObject.put("message", "Incorrect password");
                responseObject.put("status", false);

                return new ResponseEntity<>(responseObject,HttpStatus.FORBIDDEN);
            }
            
        } catch (Exception e) {
            responseObject.put("message", "Something went wrong While checking password"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



     public ResponseEntity<?> setNewPassword(String id,String password ){
            Map<String,Object> responseObject=new HashMap<>();
        try {

            Optional<User> isUserExists=this.userRepository.findById(id);

            if(isUserExists.isEmpty()){
                responseObject.put("message","User not found!");
                responseObject.put("status",false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            User user=isUserExists.get();
            String newPassword=new BCryptPasswordEncoder(12).encode(password);
            user.setPassword(newPassword);
            this.userRepository.save(user);

             Notification userNotification=new Notification();

            userNotification.setTitle("Password Updated Successfully");

             userNotification.setMessage("Your account password has been updated successfully. For security reasons, please keep your password confidential.");
             userNotification.setUser(user);
             userNotification.setCreatedAt(LocalDateTime.now());

             this.notificationService.addNotification(userNotification);

            responseObject.put("message","Password Changed.");
            responseObject.put("status",true);
            return new ResponseEntity<>(responseObject,HttpStatus.OK);
            
        } catch (Exception e) {
           responseObject.put("message", "Something went wrong While checking password"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     public ResponseEntity<?> getAllUsers(String search,String role,int page) {
         Map<String,Object> responseObject=new HashMap<>();
       try {

        String username=SecurityContextHolder.getContext().getAuthentication().getName();

        Pageable pageable=PageRequest.of(page, 6);
        Page<UserProfileDto> allUsers=this.userRepository.findAllUserWithDto(search,role,pageable,username);

        return new ResponseEntity<>(allUsers,HttpStatus.OK);

       } catch (Exception e) {
           responseObject.put("message", "Something went wrong While Fetching users"+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
       }
     }

     public ResponseEntity<?> changeAccountStatusOfUser(String userId,String status){
         Map<String,Object> responseObject=new HashMap<>();
        try {
            this.userRepository.updatedStatusOfUser(userId,status);
           responseObject.put("message", "User status updated to " + status + " successfully.");
           responseObject.put("status", true);
            return new ResponseEntity<>(responseObject,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Something went wrong Changing the status of User "+e.getMessage());
           responseObject.put("status", false);

           return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
     }




}
