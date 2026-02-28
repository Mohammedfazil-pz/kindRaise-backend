package com.example.KindRaise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation,String>{

}
