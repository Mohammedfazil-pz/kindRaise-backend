package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.dtos.CampaignImageDto;
import com.example.KindRaise.dtos.TestimonialDto;
import com.example.KindRaise.model.Testimonial;
import com.example.KindRaise.repository.TestimonialRepository;
import com.example.KindRaise.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.KindRaise.model.User;

@Service
public class TestimonialService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestimonialRepository TestimonialRepository;
    
    @Transactional
    public ResponseEntity<?> addTestimonial(Testimonial testimonial ,MultipartFile file){
        Map<String,Object> hasMap=new HashMap<>();
        try {

            String username=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=this.userRepository.findByUsername(username);
            

            if(user.getRole().equals("USER")){
                testimonial.setName(user.getFullName());
                testimonial.setRole("Supporter");
                testimonial.setCompany("KindRaise Community");
                testimonial.setStatus(false);
                testimonial.setUser(user);
            }

            if(file!=null&& !file.isEmpty()){
                testimonial.setImage(file.getBytes());
                testimonial.setImageName(file.getOriginalFilename());
                testimonial.setImageType(file.getContentType());
            }

            this.TestimonialRepository.save(testimonial);

            hasMap.put("message", "Testimonial Added successfully.");
            hasMap.put("status",false);

            return new ResponseEntity<>(hasMap,HttpStatus.CREATED);
            
        } catch (Exception e) {
           hasMap.put("message","Something went wrong while adding testimonial"+e.getMessage());
           hasMap.put("status",false);
           return new ResponseEntity<>(hasMap,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getAllTestimonials(int currentPage){
          Map<String,Object> hasMap=new HashMap<>();
        try {

            Pageable pageable=PageRequest.of(currentPage,7);
            Page<TestimonialDto> testimonials=this.TestimonialRepository.fetchAllTestimonialsWithDto(pageable);

            return new ResponseEntity<>(testimonials,HttpStatus.OK);
            
        } catch (Exception e) {
           hasMap.put("message","Something went wrong while fetching all testimonials"+e.getMessage());
           hasMap.put("status",false);
           return new ResponseEntity<>(hasMap,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> updateStatusOfTestimonial(String id,boolean status){
          Map<String,Object> hasMap=new HashMap<>();
        try {
            
            Optional<Testimonial> isTestimonial=this.TestimonialRepository.findById(id);

            if(isTestimonial.isEmpty()){
                hasMap.put("message", "testimonial not found.");
                hasMap.put("status", false);
               return new ResponseEntity<>(hasMap,HttpStatus.NOT_FOUND);
            }

            Testimonial testimonial=isTestimonial.get();

            testimonial.setStatus(status);

            this.TestimonialRepository.save(testimonial);

            hasMap.put("message","Testimonial status updated successfully.");
            hasMap.put("status",true);

            return new ResponseEntity<>(hasMap,HttpStatus.OK);
            
        } catch (Exception e) {
           hasMap.put("message","Something went wrong while fetching all testimonials"+e.getMessage());
           hasMap.put("status",false);
           return new ResponseEntity<>(hasMap,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> fetchActiveTestimonials(){
          Map<String,Object> hasMap=new HashMap<>();
        try {
            
            List<TestimonialDto> testimonials=this.TestimonialRepository.findByStatusTrueWithDto();
            return new ResponseEntity<>(testimonials,HttpStatus.OK);
            
        } catch (Exception e) {
           hasMap.put("message","Something went wrong while fetching active testimonials"+e.getMessage());
           hasMap.put("status",false);
           return new ResponseEntity<>(hasMap,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> deleteTestimonials(String id){
          Map<String,Object> hasMap=new HashMap<>();
        try {

            this.TestimonialRepository.deleteById(id);

            hasMap.put("message","Testimonial deleted successfully.");
            hasMap.put("status",true);

            return new ResponseEntity<>(hasMap,HttpStatus.OK);
            
        } catch (Exception e) {
           hasMap.put("message","Something went wrong while fetching all testimonials"+e.getMessage());
           hasMap.put("status",false);
           return new ResponseEntity<>(hasMap,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> getTestimonialUserImage(String id) {
        Map<String,Object> responseObject=new HashMap<>();
        try {
            Optional<CampaignImageDto> imageChecking=this.TestimonialRepository.getTestimonialImage(id);
            if(imageChecking.isEmpty()){
                responseObject.put("message", "image not found");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject,HttpStatus.NOT_FOUND);
            } 

            CampaignImageDto imageData=imageChecking.get();
             
            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(imageData.getImageType())).body(imageData.getImageData());

        } catch (Exception e) {
            responseObject.put("message", "Failed while fetching campaign image "+e.getMessage());
            responseObject.put("status", false);

            return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
