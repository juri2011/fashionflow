package com.fashionflow.dto;

import com.fashionflow.entity.Member;
import com.fashionflow.entity.ProfileImage;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImageDTO {
    private int id;
    private String uploadPath;
    private String uuid;
    private String filename;
    private static ModelMapper modelMapper = new ModelMapper();
    public static ProfileImageDTO entityToDTO(ProfileImage profileImage){
        if(profileImage == null) return null;
        else return modelMapper.map(profileImage, ProfileImageDTO.class);
    }
}
