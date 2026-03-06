package com.example.KindRaise.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.DonationDto;
import com.example.KindRaise.dtos.UserDonationDto;
import com.example.KindRaise.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation,String>{
    @Query("""
            SELECT new com.example.KindRaise.dtos.DonationDto(
            d.id,d.campaign.title,d.amount,d.donationDate,d.transactionId,d.user.id,d.user.fullName
            ) FROM Donation d WHERE d.campaign.id=:id
            """)
    List<DonationDto> findByCampaignIdByDto(@Param("id") String id);

     @Query("""
            SELECT new com.example.KindRaise.dtos.UserDonationDto(
            d.id,d.amount,d.donationDate,d.transactionId,d.campaign.title,d.campaign.id
            ) FROM Donation d WHERE d.user.id=:id
            """)
    List<UserDonationDto> findDonationByUserId(@Param("id") String id);

    @Query("""
            SELECT new com.example.KindRaise.dtos.DonationDto(
            d.id,d.campaign.title,d.amount,d.donationDate,d.transactionId,d.user.id,d.user.fullName
            ) FROM Donation d 
            """)
    List<DonationDto> findAllDonationsByDto();
}