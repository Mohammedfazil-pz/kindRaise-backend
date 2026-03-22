package com.example.KindRaise.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String id;
    private String fullName;
    private String phone;
    private String role;
    private String status;
    private String username;
    private LocalDateTime createdAt;
}
