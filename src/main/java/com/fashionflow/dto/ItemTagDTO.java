package com.fashionflow.dto;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemTag;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTagDTO {

    private Long id;

    private ItemTagName itemTagName;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemTagDTO entityToDTO(ItemTag itemTag){
        ItemTagDTO itemTagDTO =  modelMapper.map(itemTag, ItemTagDTO.class);
        return itemTagDTO;
    }
}
