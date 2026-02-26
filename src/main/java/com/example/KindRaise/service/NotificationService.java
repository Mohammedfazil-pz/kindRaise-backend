package com.example.KindRaise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.KindRaise.model.Notification;
import com.example.KindRaise.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public void addNotification(Notification notification){
       this.notificationRepository.save(notification);
    }

}
