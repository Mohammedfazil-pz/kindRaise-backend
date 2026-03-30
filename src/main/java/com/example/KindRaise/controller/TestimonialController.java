package com.example.KindRaise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.KindRaise.model.Testimonial;
import com.example.KindRaise.service.TestimonialService;

@RestController
@RequestMapping("/api/testimonial")
public class TestimonialController {

    @Autowired
    TestimonialService testimonialService;
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> createTestimonial(@RequestPart(value = "info") Testimonial testimonial,
    @RequestPart(value = "image", required = false) MultipartFile file
){
        return this.testimonialService.addTestimonial(testimonial,file);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTestimonials(@RequestParam(defaultValue = "0",required=false) int page){
        return this.testimonialService.getAllTestimonials(page);
    }

    @PutMapping("/update/status/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTestimonials(@PathVariable String id,@PathVariable Boolean status){
        return this.testimonialService.updateStatusOfTestimonial(id,status);
    }

    @GetMapping("/all/active")
    public ResponseEntity<?> getAllTestimonials(){
        return this.testimonialService.fetchActiveTestimonials();
    }


    @PutMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTestimonial(@PathVariable String id){
        return this.testimonialService.deleteTestimonials(id);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getTestimonialUserImage(@PathVariable("id") String id){
        return this.testimonialService.getTestimonialUserImage(id);
    }
}
