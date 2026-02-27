package com.example.KindRaise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.model.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, String> {

    @Query("""
    SELECT new com.example.KindRaise.dtos.CampaignDto(
        c.id,
        c.title,
        c.description,
        c.goalAmount,
        c.deadline,
        c.status,
        c.user.id
    )
    FROM Campaign c
    WHERE c.user.id = :userId
    """)
    List<CampaignDto> findUserCampaigns(@Param("userId") String userId);
  @Query("""
    SELECT new com.example.KindRaise.dtos.CampaignDto(
    c.id,
    c.title,
    c.description,
    c.goalAmount,
    c.deadline,
    c.status,
    c.user.id
    )
    FROM Campaign c WHERE c.id = :id
    """)
    Optional<CampaignDto> findCampaignDtoById(@Param("id") String id);

    @Query("""
            SELECT new com.example.KindRaise.dtos.CampaignDto(
            c.id,
            c.title,
            c.description,
            c.goalAmount,
            c.deadline,
            c.status,
            c.user.id
            ) FROM Campaign c where c.status='ACTIVE'
            """)
    List<CampaignDto> findActiveCampaigns(); 
    
    // For each row in Campaign table, create a new CampaignDto object using this constructor.
    @Query("""
        
            SELECT new com.example.KindRaise.dtos.CampaignDto(
              c.id,
            c.title,
            c.description,
            c.goalAmount,
            c.deadline,
            c.status,
            c.user.id
            ) FROM Campaign c
            """)
    List<CampaignDto> findAllCampaignsWithDto();

}