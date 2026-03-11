package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.model.User;
import com.example.KindRaise.service.AuthService;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> onRegister(@RequestPart("userinfo") User user,@RequestPart("file") MultipartFile file){
        return this.authService.registerUser(user,file);
    }

    @PostMapping("/login")
    public ResponseEntity<?> onLogin(@RequestBody User user){
        System.out.println(user);
        return this.authService.loginUser(user);
    }

    @PostMapping("/checkusername/{username}")
    public ResponseEntity<?> onCheckUsername(@PathVariable("username") String username){
       
        return this.authService.onCheckUsername(username);
    }

    
}
