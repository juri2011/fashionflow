package com.fashionflow.dto;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.entity.ItemTag;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTagDTO {

    private Long id;  // 태그의 ID

    private ItemTagName itemTagName;  // 태그 이름, Enum 타입 사용

    private static final ModelMapper modelMapper = new ModelMapper();

    // ItemTag 엔티티 객체를 ItemTagDTO 객체로 변환
    public static ItemTagDTO entityToDTO(ItemTag itemTag) {
        return modelMapper.map(itemTag, ItemTagDTO.class);
    }
}
