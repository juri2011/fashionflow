package com.fashionflow.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HeartDTO {

    private Long id;
    private String itemName;
    private int price;
    private String imgName;
}
