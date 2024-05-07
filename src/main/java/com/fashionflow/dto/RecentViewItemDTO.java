package com.fashionflow.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentViewItemDTO {
    private Long itemId;
    private String itemName;
    private String oriImgName;
}
