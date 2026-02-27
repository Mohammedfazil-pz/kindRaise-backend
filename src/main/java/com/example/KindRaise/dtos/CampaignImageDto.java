package com.example.KindRaise.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignImageDto {
    private byte[] imageData;
    private String imageType;
}
