package com.example.KindRaise.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.KindRaise.dtos.UserNotificationDto;
import com.example.KindRaise.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,String>{

    @Query("""
            SELECT new com.example.KindRaise.dtos.UserNotificationDto(
            n.id,n.title,n.message,n.readStatus,n.createdAt,n.user.id
            ) FROM Notification n WHERE n.user.id=:id ORDER BY n.createdAt DESC
            """)
    Page<UserNotificationDto> findByUserIdWithDto(@Param("id") String id,Pageable pageable);
     
    @Modifying
    @Transactional
    @Query("""
            UPDATE Notification n set n.readStatus=true WHERE n.user.id=:id
            """)
    void markAllNotificationOfUserAsRead(@Param("id") String id);  
    
   @Modifying
    @Transactional
    @Query("""
            UPDATE Notification n set n.readStatus=true WHERE n.user.id IS NULL
            """)
    void markAllNotificationOfAdminAsRead();


    Long countByUserIdAndReadStatus(String userId,Boolean status);

    @Query("""
          SELECT new com.example.KindRaise.dtos.UserNotificationDto(
            n.id,n.title,n.message,n.readStatus,n.createdAt,null
            )  FROM Notification n where n.user.id IS NULL ORDER BY n.createdAt DESC
                    """)
    Page<UserNotificationDto> findByUserIdIsNullOrderByCreatedAtDesc(Pageable pageable);


    Long countByUserIdIsNullAndReadStatusIsFalse();
  

}
