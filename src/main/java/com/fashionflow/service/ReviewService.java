package com.fashionflow.service;

import com.fashionflow.constant.ReviewTagContent;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.Review;
import com.fashionflow.entity.ReviewTag;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ReviewRepository;
import com.fashionflow.repository.ReviewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemRepository itemRepository;
    private final ReviewTagRepository reviewTagRepository;

    // 상품 리뷰 및 이미지 목록 가져오기
    public List<ReviewDTO> getItemReviewListWithImg(String userEmail) {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        // 현재 사용자의 이메일로 등록된 리뷰를 최근에 작성된 순서로 가져옴
        // 시도 1 : 메소드명을 변경하여 Review 엔티티의 MemberEmail 필드로 데이터 조회
        // 결과 : MemberEmail은 구매자 정보를 가져오므로 실패
        //List<Review> reviews = reviewRepository.findReviewsByMemberEmailOrderByRegDateDesc(userEmail);

        //시도 2 : Review 엔티티의 ItemId 필드로 Item 엔티티 조회한 후 Item 엔티티 안의 Member 필드 안의 MemberEmail이 같다면
        //(즉 현재 이메일과 상품 판매자 이메일이 같다면) 데이터 조회
        //결과 : 상품을 삭제한 경우 출력이 안되므로 실패
        /*List<Review> reviews = reviewRepository.findAll().stream().filter(review -> {
            Optional<Item> item = itemRepository.findById(review.getItemId());
            return item.isPresent() && item.get().getMember().getEmail().equals(userEmail);
        }).toList();*/

        //시도 3: Review 엔티티에 seller_email 필드를 넣어서 조회
        List<Review> reviews = reviewRepository.findBySellerEmailOrderByRegDateDesc(userEmail);

        // 리뷰 목록 출력
        reviews.forEach(review -> System.out.println("==========================" + review));

        for (Review review : reviews) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(review.getItemId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            // 이미지가 존재하면 해당 경로를 사용하고, 그렇지 않으면 기본 이미지 경로를 사용합니다.
            String imgName = img != null ? "/images/"+img.getImgName() : "/img/default.PNG";

            Item item = itemRepository.findById(review.getItemId()).orElse(null);


            ReviewDTO dto = new ReviewDTO();
            dto.setId(review.getItemId());
            dto.setItemName(item != null ? item.getItemName() : null);
            dto.setPrice(item != null ? item.getPrice() : 0);
            dto.setContent(review.getContent());
            dto.setScore(review.getScore());
            dto.setImgName(imgName);
            dto.setRegDate(review.getRegDate());

            // ReviewTag 정보 가져오기
            List<ReviewTag> reviewTags = reviewTagRepository.findByReview(review);
            List<ReviewTagContent> tagContents = reviewTags.stream()
                    .map(ReviewTag::getReviewTagContent)
                    .collect(Collectors.toList());
            dto.setReviewTags(tagContents);

            reviewDTOs.add(dto);
        }

        return reviewDTOs;
    }
}
