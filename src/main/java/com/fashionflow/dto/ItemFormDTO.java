package com.fashionflow.dto;

import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.entity.Item;
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

    @Builder.Default // Builder로 인스턴스 생성 시 초기값 지정
    private LocalDateTime regdate = LocalDateTime.now(); // 등록일을 현재 시간으로 초기화

    private Integer viewCount; //조회수

    //private Category category; //카테고리
    private CategoryDTO categoryDTO;

    private ItemStatus itemStatus; //상품 상태

    private SellStatus sellStatus; //판매 상태

    private Long memberId; //판매자 아이디

    /* ******************** */
    @Builder.Default //Builder로 인스턴스 생성시 초기값 지정
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>(); //상품 이미지 리스트

    private ItemImgDTO itemRepImgDTO; //대표 이미지
    /* ******************** */

    @Builder.Default
    private List<ItemTagDTO> itemTagDTOList = new ArrayList<>(); //상품 태그 리스트

    /* 지금은 사용되지 않는 것 같습니다. 필요하실 때 주석 해제하시면 됩니다
    @Builder.Default
    private List<Long> itemImgIds = new ArrayList<>(); //상품 이미지 번호 리스트
     */

    private static ModelMapper modelMapper = new ModelMapper();

    //ItemFormDTO 형을 Item형으로 변환
    public Item createItem(){return modelMapper.map(this, Item.class);}

    //Item형을 ItemFormDTO형으로 변환
    public static ItemFormDTO of (Item item) {
        ItemFormDTO itemFormDTO = modelMapper.map(item, ItemFormDTO.class);
        //CategoryDTO 에 상위 카테고리가 있다면 상위 카테고리의 정보도 저장함
        itemFormDTO.setCategoryDTO(CategoryDTO.entityToDto(item.getCategory()));
        //Member 객체의 아이디를 저장함
        itemFormDTO.setMemberId(item.getMember().getId());
        return itemFormDTO;
    }
}
