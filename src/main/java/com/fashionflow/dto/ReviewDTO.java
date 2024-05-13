package com.fashionflow.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewDTO {

    private Long id;

    private String itemName;

    private String content;

    private int price;

    private int score;

    private String imgName;

    private LocalDateTime regDate;

}
