package com.example.KindRaise.service;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

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
        Map<String,Object> responseObject=new HashMap<>();
        try {
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
           
            User user=this.userRepository.findUserWithoutImage(username);

            Pageable pageable=PageRequest.of(page, size);

            Page<UserNotificationDto> notifications=this.notificationRepository.findByUserIdWithDto(user.getId(),pageable);

            responseObject.put("notifications", notifications.getContent());
            responseObject.put("currentPage", notifications.getNumber());
            responseObject.put("totalPages", notifications.getTotalPages());
            responseObject.put("totalItems", notifications.getTotalElements());

            return new ResponseEntity<>(notifications,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while fetching notifications"+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        
    }


    public ResponseEntity<?> markAsReadNotification(String id) {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Optional<Notification> isNotificationExists= this.notificationRepository.findById(id);
            if(isNotificationExists.isEmpty()){
                 responseObject.put("message","Notification not found");
                 responseObject.put("status",false);
                 return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            }

            Notification notification=isNotificationExists.get();
            notification.setReadStatus(true);
            this.notificationRepository.save(notification);
            responseObject.put("message","Notification status updated to read.");
            responseObject.put("status",true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong While Marking as Read "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> markAllNotificationOfUserAsRead(){
        Map<String,Object> responseObject=new HashMap<>();
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findUserWithoutImage(username);
            this.notificationRepository.markAllNotificationOfUserAsRead(user.getId());
            responseObject.put("message","Marked all notifications as read");
            responseObject.put("status",true);
            
            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong While Marking as Read "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> countTotalNumberOfNotificationsUnRead(){
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Long result;
            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findUserWithoutImage(username);

            if(user.getRole().equals("USER")){
                result=this.notificationRepository.countByUserIdAndReadStatus(user.getId(), false);
            }else{
                 result=this.notificationRepository.countByUserIdIsNullAndReadStatusIsFalse();
            }
 
             responseObject.put("total",result);
            responseObject.put("status",true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
            responseObject.put("message", "Something went wrong While Couting the notifications "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAdminNotifications(int currentPage,int size){
          Map<String,Object> responseObject=new HashMap<>();
        try {
            Pageable pageable=PageRequest.of(currentPage, size);
            Page<UserNotificationDto> adminNotifications=this.notificationRepository.findByUserIdIsNullOrderByCreatedAtDesc(pageable);
            return new ResponseEntity<>(adminNotifications,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while fetching notifications"+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> markAllAdminNotificationAsRead(){
          Map<String,Object> responseObject=new HashMap<>();
        try {
            this.notificationRepository.markAllNotificationOfAdminAsRead();
            responseObject.put("message","Marked all notifications as read");
            responseObject.put("status",true);
            return new ResponseEntity<>(responseObject,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Something went wrong while Marking All Read "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
