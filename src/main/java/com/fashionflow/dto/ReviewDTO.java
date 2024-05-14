package com.fashionflow.dto;

import com.fashionflow.constant.ReviewTagContent;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private String content;
    private int score;
    private List<ReviewTagContent> reviewTags; // 추가
}
