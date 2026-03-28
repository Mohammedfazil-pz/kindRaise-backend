package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.model.User;
import com.example.KindRaise.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> fetchProfile(){
        return this.userService.fetchProfile();
    }

    @GetMapping("/profile/image/{id}")
    public ResponseEntity<?> fetchProfileImage(@PathVariable String id){
       return this.userService.fetchProfileImage(id);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> fetchProfileById(@PathVariable String id){
        return this.userService.fetchProfileById(id);
    }

    @PutMapping("/update/profile/{id}")
    public ResponseEntity<?> updateProfile(
        @PathVariable String id,
        @RequestPart("info") User user,
        @RequestPart(value = "file",required = false) MultipartFile file){
            return this.userService.updateProfile(id, user, file);
        }

    @PostMapping("/check/password/{id}/{password}")
    public ResponseEntity<?> checkPassword(@PathVariable String id,@PathVariable String password){
        return this.userService.checkPassword(id, password);
    }  
    
    @PutMapping("/change/password/{id}/{password}")
    public ResponseEntity<?> setNewPassword(@PathVariable String id,@PathVariable String password){
        return this.userService.setNewPassword(id, password);
    }  


    @GetMapping("/all")
      @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
    @RequestParam(defaultValue = "") String search,
    @RequestParam(defaultValue = "") String role,
    @RequestParam(defaultValue="0") int page){
        return this.userService.getAllUsers(search,role,page);
    }

     @PutMapping("/updatestatus/{userId}/{status}")
       @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeAccountStatusOfUser(@PathVariable String userId,@PathVariable String status){
        return this.userService.changeAccountStatusOfUser(userId, status);
    } 

     @GetMapping("/all/once")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsersInOneCall(){
        return this.userService.getAllUsersInOneCall();
    } 


}
