package com.fashionflow.service;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.ItemBuyRepository;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final ItemBuyRepository itemBuyRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final ItemImgRepository itemImgRepository;



    //구매한 아이템 리스트
    public List<ItemBuy> getItemBuyList() {
        Long memberId = memberService.findMemberByCurrentEmail().getId();
        Sort sort = Sort.by(Sort.Direction.DESC, "buyDate"); // 거래일 내림차순 정렬
        return itemBuyRepository.findByMemberId(memberId, sort);
    }

    //구매한 아이템 리스트 이미지 추가
    public List<BuyerDTO> getItemBuyListWithImg() {
        List<BuyerDTO> buyerDTO = new ArrayList<>();

        for (ItemBuy itemBuy : getItemBuyList()) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(itemBuy.getItem().getId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            BuyerDTO dto = new BuyerDTO(itemBuy.getItem().getId(), itemBuy.getItem().getItemName(), itemBuy.getItem().getPrice(), img.getOriImgName(), itemBuy.getBuyDate(), itemBuy.isReviewExists());
            buyerDTO.add(dto);
        }

        return buyerDTO;
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