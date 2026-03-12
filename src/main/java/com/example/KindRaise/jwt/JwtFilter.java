package com.example.KindRaise.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.KindRaise.security.CustomizedUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String extractedTokenFromHeader=this.jwtUtils.extractTokenFromHeader(request);
          ;
            if(extractedTokenFromHeader!=null&&this.jwtUtils.validateToken(extractedTokenFromHeader)){
              
                String username=this.jwtUtils.extractUserIdFromToken(extractedTokenFromHeader);
             
                UserDetails userDetails=this.customizedUserDetailsService.loadUserByUsername(username);
               
                Authentication userObjectForSecurityHolder=new UsernamePasswordAuthenticationToken
                (userDetails.getUsername(),null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(userObjectForSecurityHolder);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        filterChain.doFilter(request, response);
    }
    
}
