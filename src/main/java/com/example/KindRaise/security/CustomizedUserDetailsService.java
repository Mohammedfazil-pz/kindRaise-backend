package com.example.KindRaise.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.KindRaise.repository.UserRepository;

@Service
public class CustomizedUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.KindRaise.model.User user= this.userRepository.findByUsername(username);
        if(user!=null){
            return User.builder().username(user.getUsername()).password(user.getPassword()).build();
        }
        throw new UsernameNotFoundException("Invalid credentials");
    }

}
