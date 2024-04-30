package com.fashionflow.dto;

import com.fashionflow.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDTO extends MemberFormDTO{

    private List<ItemFormDTO> itemFormDTOList = new ArrayList<>(); //판매중인 상품 목록
/*
    @Builder.Default
    private List<ItemImgDTO> itemRepImgDTOList = new ArrayList<>();
*/
    private Long sellCount; //판매수

    //Builder로는 부모 객체의 필드에 값을 저장할 수 없으므로 setter를 사용...
    public static MemberDetailDTO entityToDTOSafe(Member member){
        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();
        memberDetailDTO.setName(member.getName());
        memberDetailDTO.setEmail(member.getEmail());
        memberDetailDTO.setNickname(member.getNickname());
        memberDetailDTO.setRegdate(member.getRegdate());
        memberDetailDTO.setIntro(member.getIntro());
        memberDetailDTO.setMannerScore(member.getMannerScore());

        return memberDetailDTO;
    }

}
