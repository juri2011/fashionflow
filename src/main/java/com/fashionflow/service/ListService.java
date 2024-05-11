package com.fashionflow.service;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;


    public Page<ListingItemDTO> listingItemWithImg(Pageable pageable) {

        Page<Item> listingItems = itemRepository.findAll(pageable);


        List<ListingItemDTO> listingItemDTO = new ArrayList<>();

        for (Item listingItem : listingItems) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(listingItem.getId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            // 임시 기본 이미지
            String imgName = img != null ? img.getImgName() : "defaultImg.png";


            // listingItemDTO 생성자에 올바른 값 전달
            ListingItemDTO dto = ListingItemDTO.builder()
                    .id(listingItem.getId())
                    .itemName(listingItem.getItemName())
                    .price(listingItem.getPrice())
                    .regdate(listingItem.getRegdate())
                    .itemStatus(listingItem.getItemStatus())
                    .sellStatus(listingItem.getSellStatus())
                    .imgName(imgName)
                    .build();

            listingItemDTO.add(dto);
        }

        return new PageImpl<>(listingItemDTO, pageable, listingItems.getTotalElements());
    }
}
