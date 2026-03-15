package com.example.KindRaise.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {
    private String id;
    private String title;
    private String message;
    private boolean readStatus;
    private LocalDateTime date;
    private String user_id;
}
