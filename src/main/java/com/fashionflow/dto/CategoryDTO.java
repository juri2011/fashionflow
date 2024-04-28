package com.fashionflow.dto;

import com.fashionflow.entity.Category;
import com.fashionflow.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id; //카테고리 id

    private String code; //카테고리 코드

    private String name; //카테고리명

    //private Long parent_id; //상위 카테고리(자신이 하위 카테고리일때만)
    private CategoryDTO parent; //상위 카테고리(자신이 하위 카테고리일때만)

    private Integer listOrder; //하위 메뉴 리스트 순서

    private static ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO entityToDto(Category category){
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        //categoryDTO.setParent_id(category.getParent().getId());
        Category parent = category.getParent();
        if(parent != null){
            categoryDTO.setParent(CategoryDTO.entityToDto(parent));
        }
        return categoryDTO;
    }
}
