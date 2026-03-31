package com.example.KindRaise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.KindRaise.dtos.CampaignImageDto;
import com.example.KindRaise.dtos.TestimonialDto;
import com.example.KindRaise.model.Testimonial;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial,String>{
    

    @Query("""
            SELECT new com.example.KindRaise.dtos.TestimonialDto(
            t.id,
            t.name,
            t.company,
            t.role,
            t.message,
            t.rating,
            t.status,
            t.user.id
            ) FROM Testimonial t ORDER BY t.status DESC
            """)
    Page<TestimonialDto> fetchAllTestimonialsWithDto(Pageable pageable);

    @Query("""
            SELECT new com.example.KindRaise.dtos.TestimonialDto(
            t.id,
            t.name,
            t.company,
            t.role,
            t.message,
            t.rating,
            t.status,
            t.user.id
            ) FROM Testimonial t WHERE t.status=true ORDER BY t.createdAt DESC
            """)
    List<TestimonialDto> findByStatusTrueWithDto();


    @Query("""
            SELECT new com.example.KindRaise.dtos.CampaignImageDto(
            t.image,t.imageType
            ) FROM Testimonial t WHERE t.id=:id
            """)
    Optional<CampaignImageDto> getTestimonialImage(@Param("id") String id);

}
