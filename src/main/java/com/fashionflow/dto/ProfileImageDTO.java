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
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;

    // ModelMapper 객체는 이 클래스 내부에서 생성되어야 함
    private static ModelMapper modelMapper = new ModelMapper();

    // ProfileImage 엔티티를 DTO로 변환하는 메서드
    public static ProfileImageDTO entityToDTO(ProfileImage profileImage){
        if(profileImage == null) return null;
        else return modelMapper.map(profileImage, ProfileImageDTO.class);
    }

    // 다른 필드들을 매핑할 필요 없으므로 제거
}
