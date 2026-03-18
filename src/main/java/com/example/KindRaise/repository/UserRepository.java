package com.example.KindRaise.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.UserProfileImageDto;
import com.example.KindRaise.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>  {

    User findByUsername(String username);

    User findByPhone(String phone);

    @Query("""
        SELECT new com.example.KindRaise.model.User(
        u.id,
        u.username,
        u.password,
        u.role,
        u.phone,
        u.fullName,
        u.DOB,
        null,
        null,
        null,
        u.createdAt
        )
        FROM User u
        WHERE u.username = :username
        """)
     User findUserWithoutImage(@Param("username") String username);



     @Query("""
        SELECT new com.example.KindRaise.model.User(
        u.id,
        u.username,
        u.password,
        u.role,
        u.phone,
        u.fullName,
        u.DOB,
        null,
        null,
        null,
        u.createdAt
        )
        FROM User u
        WHERE u.id = :id
        """)
     User findUserByIdWithoutImage(@Param("id") String id);


     @Query("""
             SELECT new com.example.KindRaise.dtos.UserProfileImageDto(
             u.imageData,u.imageType
             ) FROM User u WHERE u.id=:id 
             """)
      UserProfileImageDto findUserProfileImageWithDto(@Param("id") String id);  
      
      
      @Query("""
        SELECT new com.example.KindRaise.model.User(
        u.id,
        u.username,
        null,
        u.role,
        u.phone,
        u.fullName,
        u.DOB,
        null,
        null,
        null,
        u.createdAt
        )
        FROM User u WHERE role='USER' ORDER BY u.createdAt DESC
        """)
      List<User> findAllUserWithoutImage(Pageable pageable);


      Long countByRole(String role);

   
}
