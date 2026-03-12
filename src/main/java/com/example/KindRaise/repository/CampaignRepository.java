package com.example.KindRaise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.CampaignDto;
import com.example.KindRaise.dtos.CampaignImageDto;
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
        c.category.id,
        c.category.title,
        c.status,
        c.user.id,
         java.math.BigDecimal.ZERO
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
    c.category.id,
    c.category.title,
    c.status,
    c.user.id,
COALESCE(SUM(d.amount),0)
    )
    FROM Campaign c LEFT JOIN Donation d ON c.id=d.campaign.id WHERE c.id = :id GROUP BY c.id,
    c.title,
    c.description,
    c.goalAmount,
    c.deadline,
    c.category.id,
    c.category.title,
    c.status,
    c.user.id
    """)
    Optional<CampaignDto> findCampaignDtoById(@Param("id") String id);

    @Query("""
            SELECT new com.example.KindRaise.dtos.CampaignDto(
            c.id,
            c.title,
            c.description,
            c.goalAmount,
            c.deadline,
            c.category.id,
            c.category.title,
            c.status,
            c.user.id,
             java.math.BigDecimal.ZERO
            ) FROM Campaign c WHERE c.status='ACTIVE' AND(
            COALESCE(:search,'') = '' OR c.title LIKE CONCAT('%',:search,'%') OR c.description LIKE CONCAT('%',:search,'%') 
            ) AND( :category IS NULL OR c.category.id=:category)
            """)
    List<CampaignDto> findActiveCampaigns(@Param("search") String search,@Param("category") String category); 
    
    // For each row in Campaign table, create a new CampaignDto object using this constructor.
    @Query("""
        
            SELECT new com.example.KindRaise.dtos.CampaignDto(
              c.id,
            c.title,
            c.description,
            c.goalAmount,
            c.deadline,
            c.category.id,
            c.category.title,
            c.status,
            c.user.id,
             java.math.BigDecimal.ZERO
            ) FROM Campaign c
            """)
    List<CampaignDto> findAllCampaignsWithDto();
    @Query("""
            SELECT new com.example.KindRaise.dtos.CampaignImageDto(c.imageData,c.imageType) FROM Campaign c WHERE c.id=:id
            """)
    Optional<CampaignImageDto> getCampaignImage(@Param("id") String id);

}