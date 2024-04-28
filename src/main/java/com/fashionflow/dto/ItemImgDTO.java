package com.fashionflow.dto;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ItemImgDTO {

    private Long id;

    private String imgName;      //이미지 파일명

    private String oriImgName;  //원본 이미지 파일

    private String imgUrl;      //조회 경로

    private String repimgYn;     //대표 이미지 여부

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDTO entityToDto(ItemImg itemImg){
        ItemImgDTO itemImgDTO = modelMapper.map(itemImg, ItemImgDTO.class);
        return itemImgDTO;
    }

}
