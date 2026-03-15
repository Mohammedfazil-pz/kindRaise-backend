package com.example.KindRaise.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.UserNotificationDto;
import com.example.KindRaise.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,String>{

    @Query("""
            SELECT new com.example.KindRaise.dtos.UserNotificationDto(
            n.id,n.title,n.message,n.readStatus,n.createdAt,n.user.id
            ) FROM Notification n WHERE n.user.id=:id
            """)
    Page<UserNotificationDto> findByUserIdWithDto(@Param("id") String id,Pageable pageable);

}
