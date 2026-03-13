package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.UserProfileImageDto;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


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
                responseObject.put("message", "Please login to fecth profile data");
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



}
