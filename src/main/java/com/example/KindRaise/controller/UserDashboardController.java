package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.service.UserDashboardService;

@RestController
@RequestMapping("/api/user/dashboard")
public class UserDashboardController {

    @Autowired
    UserDashboardService userDashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<?> userDashboardStats(){
        return this.userDashboardService.userDashboardStats();
    }
}
