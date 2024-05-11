package com.fashionflow.dto;

import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.entity.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingItemDTO {

    private Long id;

    private String itemName;

    private int price;

    private LocalDateTime regdate;

    private ItemStatus itemStatus;

    private SellStatus sellStatus;

    private String imgName;
}
