package com.example.KindRaise.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.CampaignTopRaisedDto;
import com.example.KindRaise.dtos.CampaignTotalDonationDto;
import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.dtos.DonationUserStatsDto;
import com.example.KindRaise.dtos.UserDonationDto;
import com.example.KindRaise.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation,String>{
    @Query("""
            SELECT new com.example.KindRaise.dtos.DonationDto(
            d.id,d.campaign.title,d.amount,d.createdAt,d.transactionId,d.user.id,d.user.fullName,d.campaign.id
            ) FROM Donation d WHERE d.campaign.id=:id
            """)
    List<DonationDto> findByCampaignIdByDto(@Param("id") String id);

     @Query("""
            SELECT new com.example.KindRaise.dtos.UserDonationDto(
           d.campaign.title,d.campaign.description,COALESCE(SUM(d.amount),0),
           (SELECT SUM(d2.amount) FROM Donation d2 WHERE d2.campaign.id=d.campaign.id),
           d.campaign.goalAmount,d.campaign.id,d.campaign.deadline
            ) FROM Donation d  WHERE d.user.id=:id GROUP BY 
             d.campaign.title,d.campaign.id,d.campaign.description,d.campaign.goalAmount,d.campaign.deadline
            """)
    Page<UserDonationDto> findDonationByUserId(@Param("id") String id,Pageable pageable);

    @Query("""
            SELECT new com.example.KindRaise.dtos.DonationDto(
            d.id,d.campaign.title,d.amount,d.createdAt,d.transactionId,d.user.id,d.user.fullName,d.campaign.id
            ) FROM Donation d WHERE COALESCE(:search,'') = '' OR 
             LOWER(d.user.fullName) LIKE LOWER(CONCAT('%',:search,'%')) OR 
             LOWER(d.campaign.title) LIKE LOWER(CONCAT('%',:search,'%'))
            ORDER BY d.createdAt DESC
            """)
    Page<DonationDto> findAllDonationsByDto(Pageable pageable,String search);

    @Query("""
        SELECT new com.example.KindRaise.dtos.CampaignTotalDonationDto(
        d.campaign.id,SUM(d.amount)
        ) FROM Donation d GROUP BY d.campaign.id
        """)
    List<CampaignTotalDonationDto> findTotalDonationsOfCampaign();

    
    @Query("""
        SELECT new com.example.KindRaise.dtos.DonationUserStatsDto(
        COALESCE(SUM(d.amount),0),COUNT(d),COUNT(DISTINCT d.campaign.id)
        ) FROM Donation d WHERE d.user.id=:id 
        """)
    DonationUserStatsDto findDonationUserStatsDto(@Param("id") String userId);    

    Long countByUserId(String donorId);   
    
        @Query("""
                  SELECT new com.example.KindRaise.dtos.DonationDto(
                 d.id,d.campaign.title,d.amount,d.createdAt,d.transactionId,d.user.id,null,d.campaign.id
                ) FROM Donation d WHERE d.user.id=:id ORDER BY d.donationDate DESC LIMIT 1
                """)
                DonationDto findTopByUserIdOrderByDonationDateDesc(@Param("id")String donorId);


        @Query(value = """
                 SELECT TO_CHAR(d.donation_date, 'YYYY-MM') as month,
                 COALESCE(SUM(d.amount), 0) as totalAmount
                 FROM donation d
                        WHERE d.donor_id = :id
                        GROUP BY month
                        ORDER BY month
                """, nativeQuery = true)
        List<Object[]> userDonationsPerMonth(@Param("id") String userId);


         @Query(value = """
                 SELECT TO_CHAR(d.donation_date, 'YYYY-MM') as month,
                 COALESCE(SUM(d.amount), 0) as totalAmount
                 FROM donation d
                        GROUP BY month
                        ORDER BY month
                """, nativeQuery = true)
        List<Object[]> totalDonationsPerMonth();


         @Query("""
                SELECT SUM(d.amount) FROM Donation d
                """)
        BigDecimal totalAmountRaised();

        @Query("""
                SELECT SUM(d.amount) FROM Donation d WHERE d.user.id=:id
                """)
        BigDecimal totalAmountUserRaised(@Param("id") String id);

        @Query("""
                SELECT COALESCE(SUM(d.amount), 0)  FROM Donation d WHERE d.createdAt BETWEEN :start AND :end
                """)
        BigDecimal findTodayDonations(LocalDateTime start, LocalDateTime end);

        @Query("""
                SELECT COUNT(DISTINCT d.user.id) FROM Donation d 
                        """)
        Long countTotalDonors();

        @Query("""
                SELECT COUNT(DISTINCT d.campaign.id) FROM Donation d WHERE d.user.id=:id
                        """)
        Long totalCampaignsSupported(String id);


        @Query("""
                SELECT new com.example.KindRaise.dtos.CampaignTopRaisedDto(
                d.campaign.id,d.campaign.title,COALESCE(SUM(d.amount),0),d.campaign.goalAmount
                ) FROM Donation d GROUP BY d.campaign.id,d.campaign.title,d.campaign.goalAmount ORDER BY
                 COALESCE(SUM(d.amount),0) DESC LIMIT 3
                        """)

        List<CampaignTopRaisedDto> findTopThreeRaisedCampaigns();  
        
        
        @Query("""
                 SELECT new com.example.KindRaise.dtos.DonationDto(
                 d.id,d.campaign.title,d.amount,d.createdAt,d.transactionId,d.user.id,d.user.fullName,d.campaign.id
                ) FROM Donation d ORDER BY d.createdAt DESC LIMIT 3
                        """)
        List<DonationDto> findRecentThreeDonations();


        @Query("""
                 SELECT new com.example.KindRaise.dtos.DonationDto(
                 d.id,d.campaign.title,d.amount,d.createdAt,d.transactionId,d.user.id,d.user.fullName,d.campaign.id
                ) FROM Donation d ORDER BY d.createdAt DESC 
                        """)
        List<DonationDto> fetchAllDonations();


        @Query(value = """
    SELECT TO_CHAR(d.donation_date, 'YYYY-MM-DD') as day,
           COALESCE(SUM(d.amount), 0) as totalAmount
    FROM donation d
    WHERE  d.donation_date >= CURRENT_DATE - INTERVAL '7 days'
    GROUP BY day
    ORDER BY day
""", nativeQuery = true)
List<Object[]> dailyDonations();
        

    
}