package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.KindRaise.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification,String>{

}
