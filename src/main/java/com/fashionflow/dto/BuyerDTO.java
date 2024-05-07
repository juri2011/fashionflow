package com.fashionflow.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerDTO {

    private Long id;

    private String itemName;

    private int price;

    private String oriImgName;

    private LocalDateTime buyDate;

    private boolean reviewExists;
}
