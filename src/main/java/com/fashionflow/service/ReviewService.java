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

   /* public List<Review> getMyReviews() {
        // 현재 로그인한 회원의 아이디 가져오기
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 현재 로그인한 회원이 등록한 상품들의 리뷰를 가져옵니다.
        List<Review> myReviews = reviewRepository.findReviewsByItemMemberEmail(currentUsername);

        return myReviews;
    }*/

    public List<ReviewDTO> getItemReviewListWithImg() {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        for (Review review : reviewRepository.findAll()) {
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
