package com.example.KindRaise.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialDto {
    private String id;
    private String name;
    private String company;
    private String role;
    private String message;
    private Integer rating;
    private Boolean status;
    private String user_id;
}
