package com.example.KindRaise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.KindRaise.jwt.CustomizedAuthEntryPoint;
import com.example.KindRaise.jwt.JwtFilter;
import com.example.KindRaise.security.CustomizedUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringCustomizedConfig {

    @Autowired
    CustomizedAuthEntryPoint customizedAuthEntryPoint;

    @Autowired
    CustomizedUserDetailsService customizedUserDetailsService;

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.cors(cors->{});
        httpSecurity.csrf(csrf->csrf.disable());
        httpSecurity.authorizeHttpRequests(httpRequests->httpRequests
            .requestMatchers("/api/register","/api/login","/api/admin/create/admin","/api/checkusername/{username}")
            .permitAll()
            .anyRequest()
            .authenticated());
        httpSecurity.exceptionHandling(exception->exception.authenticationEntryPoint(customizedAuthEntryPoint));
        httpSecurity.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(this.customizedUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

}
