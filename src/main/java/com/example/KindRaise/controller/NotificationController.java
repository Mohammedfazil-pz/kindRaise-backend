package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.service.NotificationService;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    
    @GetMapping("/user")
    public ResponseEntity<?> getUserNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size){
        return this.notificationService.getUserNotifications(page,size);
    }

    @PutMapping("/read/notification/{id}")
    public ResponseEntity<?> markAsReadNotification(@PathVariable String id){
        System.out.println(id);
        return this.notificationService.markAsReadNotification(id);
    }

    @PutMapping("/read/all/notification")
    public ResponseEntity<?> markAllNotificationOfUserAsRead(){
        return this.notificationService.markAllNotificationOfUserAsRead();
    }
}
