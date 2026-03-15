package com.example.KindRaise.service;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.UserNotificationDto;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.NotificationRepository;
import com.example.KindRaise.repository.UserRepository;



@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    public void addNotification(Notification notification){
       this.notificationRepository.save(notification);
    }


    public ResponseEntity<?> getUserNotifications(int page,int size){
        Map<String,Object> hashMap=new HashMap<>();
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(username);
            User user=this.userRepository.findUserWithoutImage(username);

            Pageable pageable=PageRequest.of(page, size);

            Page<UserNotificationDto> notifications=this.notificationRepository.findByUserIdWithDto(user.getId(),pageable);

            hashMap.put("notifications", notifications.getContent());
            hashMap.put("currentPage", notifications.getNumber());
            hashMap.put("totalPages", notifications.getTotalPages());
            hashMap.put("totalItems", notifications.getTotalElements());

            return new ResponseEntity<>(notifications,HttpStatus.OK);

        } catch (Exception e) {
            hashMap.put("message", "Something went wrong "+e.getMessage());
            hashMap.put("status", false);

            return new ResponseEntity<>(hashMap,HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        
    }

}
