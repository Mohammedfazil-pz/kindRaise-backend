package com.example.KindRaise.service;



import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.jwt.JwtUtils;
import com.example.KindRaise.model.Notification;
import com.example.KindRaise.model.User;
import com.example.KindRaise.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    NotificationService notificationService;



     Map<String,Object> responseObject=new HashMap<>();

    public ResponseEntity<?> registerUser(User user,MultipartFile file){
        Notification adminNotification=new Notification();
        try {
           User isAlreadyExistsUsername=this.userRepository.findByUsername(user.getUsername());
           if(isAlreadyExistsUsername!=null){
            responseObject.put("message", "Username already exists!");
            responseObject.put("status",false);
            return new ResponseEntity<>(responseObject,HttpStatus.UNAUTHORIZED);
           }

           User isAlreadyExistsPhone=this.userRepository.findByPhone(user.getPhone());
           if(isAlreadyExistsPhone!=null){
            responseObject.put("message", "Phone number already registered!");
            responseObject.put("status",false);
            return new ResponseEntity<>(responseObject,HttpStatus.UNAUTHORIZED);
           }

           user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));

           user.setImageData(file.getBytes());
           user.setImageName(file.getOriginalFilename());
           user.setImageType(file.getContentType());

           

           this.userRepository.save(user);

            adminNotification.setTitle("New Account Registration");
            adminNotification.setMessage("A new user with username : "+user.getUsername() +" has been successfully created an account");

            responseObject.put("message", "Your account has been registered successfully.");
            responseObject.put("status",true);
            responseObject.put("role", user.getRole());
            responseObject.put("username", user.getUsername());

           return new ResponseEntity<>(responseObject,HttpStatus.CREATED);


        } catch (Exception e) {
            responseObject.put("message", e.getMessage());
            responseObject.put("status",false);
            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createUser(User user,MultipartFile file){

         Notification adminNotification=new Notification();

          try {
             User isAlreadyExistsUsername=this.userRepository.findByUsername(user.getUsername());

            if(isAlreadyExistsUsername!=null){
                responseObject.put("message", "Username already exists!");
                responseObject.put("status",false);
                return new ResponseEntity<>(responseObject,HttpStatus.UNAUTHORIZED);
            }
            
            
            user.setImageName(file.getOriginalFilename());
            user.setImageData(file.getBytes());
            user.setImageType(file.getContentType());
            user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));

            User userObject=this.userRepository.save(user);

            adminNotification.setTitle(user.getRole() + " Account Created");

            adminNotification.setMessage(
                user.getRole() + " '" + user.getUsername() + "' has been successfully added."
            );
             this.notificationService.addNotification(adminNotification);

          if (user.getRole().equals("USER")) {

                Notification userNotification = new Notification();

                userNotification.setTitle("Welcome to KindRaise 🎉");

                userNotification.setMessage(
                    "Your account has been created by the admin. Welcome to KindRaise, " 
                    + user.getUsername() + "! You can now explore and get started."
                );

                userNotification.setUser(userObject); 
                this.notificationService.addNotification(userNotification);
            }

            responseObject.put("message", "User Created SuccessFully!");
            responseObject.put("status",true);
            responseObject.put("role", user.getRole());
            responseObject.put("username", user.getUsername());

           return new ResponseEntity<>(responseObject,HttpStatus.CREATED);

          } catch (Exception e) {
            responseObject.put("message", e.getMessage());
            responseObject.put("status",false);
            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
          }

    }

    @Transactional
    public ResponseEntity<?> loginUser(User user) {
        
        try {
             User existingUser = userRepository.findByUsername(user.getUsername());

            if(existingUser == null){
                responseObject.put("message","User not found. Please register first.");
                responseObject.put("status",false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
         }

         System.out.println(existingUser.getStatus());

         if(!existingUser.getStatus().equals("ACTIVE")){
                 responseObject.put("message","User account is inactive");
                responseObject.put("status",false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
         }
            Authentication authentication=this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            

            String access_token=this.jwtUtils.generateTokenWithUsername(user.getUsername());

            responseObject.put("message","Login successful! Redirecting to your dashboard...");
            responseObject.put("status",true);
            responseObject.put("access_token", access_token);
            responseObject.put("role", authentication.getAuthorities());
            responseObject.put("name", existingUser.getFullName());
          

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (BadCredentialsException e) {

            responseObject.put("message", "Invalid username or password.");
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
        } 
        catch (UsernameNotFoundException e) {

            responseObject.put("message", "User not found. Please register first.");
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
        } 
        catch (Exception e) {

            responseObject.put("message", "Login failed: " + e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> onCheckUsername(String username) {
       try {
           User isUserExists=this.userRepository.findByUsername(username);
            if(isUserExists!=null){
                responseObject.put("message", "This username is already taken.");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.CONFLICT);
            }

            responseObject.put("message", "Username not found ✅");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);


       } catch (Exception e) {
            responseObject.put("message", "Username checking failed "+e.getMessage());
            responseObject.put("status",false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
