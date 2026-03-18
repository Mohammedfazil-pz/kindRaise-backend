package com.example.KindRaise.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.CampaignDashboardDto;
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
        COALESCE(SUM(d.amount),0),
        COUNT(DISTINCT d.user.id)
    )
    FROM Campaign c LEFT JOIN Donation d ON c.id=d.campaign.id WHERE c.user.id = :userId
    GROUP BY c.id, c.title,
    c.description,
    c.goalAmount,
    c.deadline,
    c.category.id,
    c.category.title,
    c.status,
    c.user.id ORDER BY 
    CASE WHEN c.status = 'ACTIVE' THEN 0 ELSE 1 END,
    c.createdAt DESC
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
    COALESCE(SUM(d.amount),0),
    COUNT(DISTINCT d.user.id)
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
             java.math.BigDecimal.ZERO,
             0L
            ) FROM Campaign c WHERE (:status IS NULL OR c.status=:status) AND(
            COALESCE(:search,'') = '' OR c.title LIKE CONCAT('%',:search,'%') OR c.description LIKE CONCAT('%',:search,'%') 
            ) AND( :category IS NULL OR c.category.id=:category)
            """)
    List<CampaignDto> findActiveCampaigns(@Param("search") String search,@Param("category") String category,@Param("status") String status); 
    
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
             java.math.BigDecimal.ZERO,
             0L
            ) FROM Campaign c ORDER BY c.createdAt DESC
            """)
    List<CampaignDto> findAllCampaignsWithDto(Pageable pageable);
    @Query("""
            SELECT new com.example.KindRaise.dtos.CampaignImageDto(c.imageData,c.imageType) FROM Campaign c WHERE c.id=:id
            """)
    Optional<CampaignImageDto> getCampaignImage(@Param("id") String id);


    @Query("""
             SELECT new com.example.KindRaise.dtos.CampaignDashboardDto(
             COUNT(DISTINCT c.id),COALESCE(SUM(d.amount),0l), COUNT(DISTINCT CASE 
              WHEN c.status='ACTIVE' THEN c.id 
             END) ) FROM Campaign c
             LEFT JOIN Donation d ON d.campaign.id=c.id WHERE c.user.id =:userId
                 """)
    CampaignDashboardDto fetchUserCampaignDashboardDetails(@Param("userId") String userId);   
    
    
    Long countByUserIdAndStatusIs(String userId,String status);

    Long countByStatus(String status);

    Long countByUserIdAndStatus(String id,String status);


   

    


}