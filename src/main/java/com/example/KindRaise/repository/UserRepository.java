package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>  {

    User findByUsername(String username);

    User findByPhone(String phone);

   
}
