package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
