package com.fashionflow.service;

import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemBuyRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final ItemBuyRepository itemBuyRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;

    //구매한 아이템 리스트
    public List<ItemBuy> getItemBuyList() {
        Long memberId = memberService.findMemberByCurrentEmail().getId();
        return itemBuyRepository.findByMemberId(memberId);
    }

    //리뷰 등록 메소드
    public void registerReview(ReviewDTO reviewDTO){

        // 리뷰 내용이 null인지 확인
        if (reviewDTO.getContent() == null || reviewDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용이 비어 있습니다.");
        }


        Member member = memberService.findMemberByCurrentEmail();
        Item item = itemRepository.findItemById(reviewDTO.getId());

        //리뷰 저장
        Review review = Review.builder()
                .item(item)
                .member(member)
                .content(reviewDTO.getContent())
                .score(reviewDTO.getScore())
                .regdate(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        //아이템 리뷰 여부 변경
        ItemBuy itemBuy = itemBuyRepository.findItemBuyByItem(item);
        if (itemBuy != null) {
            itemBuy.setReviewExists(true);
            itemBuyRepository.save(itemBuy); // 변경사항 저장
        }


    }
}