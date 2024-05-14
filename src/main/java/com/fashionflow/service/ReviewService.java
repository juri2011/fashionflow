package com.fashionflow.service;

import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final ItemImgRepository itemImgRepository;


    public List<ReviewDTO> getItemReviewListWithImg(String userEmail) {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        // 현재 사용자의 이메일로 등록된 리뷰를 최근에 작성된 순서로 가져옴
        List<Review> reviews = reviewRepository.findReviewsByItemMemberEmailOrderByRegDateDesc(userEmail);

        for (Review review : reviews) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(review.getItem().getId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            // 이미지가 존재하면 해당 경로를 사용하고, 그렇지 않으면 기본 이미지 경로를 사용합니다.
            String imgName = img != null ? "/images/"+img.getImgName() : "/img/default.PNG";

            ReviewDTO dto = new ReviewDTO();
            dto.setId(review.getItem().getId());
            dto.setItemName(review.getItem().getItemName());
            dto.setContent(review.getContent());
            dto.setPrice(review.getItem().getPrice());
            dto.setScore(review.getScore());
            dto.setImgName(imgName);
            dto.setRegDate(review.getRegDate());
            reviewDTOs.add(dto);
        }

        return reviewDTOs;
    }


}
