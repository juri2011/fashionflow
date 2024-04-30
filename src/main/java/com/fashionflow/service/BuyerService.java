package com.fashionflow.service;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemBuyRepository;
import com.fashionflow.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final ItemBuyRepository itemBuyRepository;
    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    public List<ItemBuy> getItemBuyList(){
        Long memberId = memberService.findMemberByCurrentEmail().getId();
        return itemBuyRepository.findByMemberId(memberId);
    }

    public Review checkReviews() {

        List<ItemBuy> itemBuyList = getItemBuyList();
        Long memberId = memberService.findMemberByCurrentEmail().getId();

        for (ItemBuy itemBuy : itemBuyList) {
            Long itemId = itemBuy.getId();
            Review review = reviewRepository.findByMemberIdAndItemId(memberId, itemId);
            boolean reviewExists = review != null;
            itemBuy.setReviewExists(reviewExists);
        }

        return itemBuyList;
    }

}