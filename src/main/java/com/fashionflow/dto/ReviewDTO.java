package com.fashionflow.dto;

import com.fashionflow.constant.ReviewTagContent;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import java.util.List;

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

    private List<ReviewTagContent> reviewTags; // 추가
}
