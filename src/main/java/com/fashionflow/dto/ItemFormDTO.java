package com.fashionflow.dto;

import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.entity.Category;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFormDTO {

    private Long id; //상품번호

    private String itemName; //상품이름

    private String content; //상세설명

    private int price; //상품가격

    private int delivery; //배송비

    private String address; //주소

    private LocalDateTime regdate; //등록일

    private Integer viewCount; //조회수

    private Category category; //카테고리

    private ItemStatus itemStatus; //상품 상태

    private SellStatus sellStatus; //판매 상태

    @Builder.Default //Builder로 인스턴스 생성시 초기값 지정
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

    @Builder.Default
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    //ItemFormDTO 형을 Item형으로 변환
    public Item createItem(){return modelMapper.map(this, Item.class);}

    //Item형을 ItemFormDTO형으로 변환
    public static ItemFormDTO of (Item item) {return modelMapper.map(item, ItemFormDTO.class);}
}
