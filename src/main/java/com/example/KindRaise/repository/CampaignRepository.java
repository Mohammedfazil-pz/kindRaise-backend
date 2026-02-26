package com.example.KindRaise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.model.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,String> {
    List<Campaign> findByUserId(String userID);
}
