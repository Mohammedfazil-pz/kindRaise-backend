package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.KindRaise.model.User;
import com.example.KindRaise.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMangementController {

    @Autowired
    AuthService authService;

    @PostMapping("/create/admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        return this.authService.createAdmin(user);
    }
}
