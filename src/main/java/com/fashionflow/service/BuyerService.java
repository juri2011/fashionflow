package com.fashionflow.service;

import com.fashionflow.constant.ReviewTagContent;
import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BuyerService {

    private final ItemBuyRepository itemBuyRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final ReviewTagRepository reviewTagRepository;



    //구매한 아이템 리스트
    public Page<BuyerDTO> getItemBuyListWithImg(Pageable pageable) {

        Long memberId = memberService.findMemberByCurrentEmail().getId();
        Page<ItemBuy> itemBuyPage = itemBuyRepository.findByMemberId(memberId, pageable);

        List<BuyerDTO> buyerDTO = itemBuyPage.getContent().stream().map(itemBuy -> {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(itemBuy.getItem().getId(), "Y")
                    .orElse(null);
            String imgName = img != null ? "/images/" + img.getImgName() : "/img/default.PNG";
            return new BuyerDTO(itemBuy.getItem().getId(), itemBuy.getItem().getItemName(),
                    itemBuy.getItem().getPrice(), imgName, itemBuy.getBuyDate(), itemBuy.isReviewExists());
        }).collect(Collectors.toList());

        return new PageImpl<>(buyerDTO, pageable, itemBuyPage.getTotalElements());
    }


    @Transactional //리뷰 등록 메소드
    public void registerReview(ReviewDTO reviewDTO){


        Member member = memberService.findMemberByCurrentEmail();
        Item item = itemRepository.findItemById(reviewDTO.getId());

        //리뷰 저장
        Review review = Review.builder()
                .itemId(item != null ? item.getId() : null)
                .memberEmail(member != null ? member.getEmail() : null)
                .sellerEmail(item != null ? item.getMember().getEmail() : null)
                .content(reviewDTO.getContent())
                .score(reviewDTO.getScore())
                .regDate(LocalDateTime.now())
                .build();

        review = reviewRepository.save(review);

        // ReviewDTO에서 리뷰 태그 리스트를 가져옴
        List<ReviewTagContent> reviewTags = reviewDTO.getReviewTags();

        if (reviewTags != null && !reviewTags.isEmpty()) {
            for (ReviewTagContent tagContent : reviewTags) {
                ReviewTag reviewTag = new ReviewTag();
                reviewTag.setReview(review); // 저장된 리뷰 객체 설정
                reviewTag.setReviewTagContent(tagContent); // 태그 내용 설정

                reviewTagRepository.save(reviewTag); // ReviewTag 저장
            }
        }



        //아이템 리뷰 여부 변경
        ItemBuy itemBuy = itemBuyRepository.findItemBuyByItem(item);
        if (itemBuy != null) {
            itemBuy.setReviewExists(true);
            itemBuyRepository.save(itemBuy); // 변경사항 저장
        }

        // seller의 mannerScore 업데이트
        Member seller = item.getMember();
        // 모든 판매자 아이템 리스트
        List<Item> sellerItemList = itemRepository.findByMemberId(seller.getId());
        // 현재 매너점수
        Double currentScore = seller.getMannerScore();

        // 리뷰된 판매자 아이템 리스트
        List<Item> reviewedSellerItemList = new ArrayList<>();

        for(Item sellerItem : sellerItemList) {
            ItemBuy reviewedItem = itemBuyRepository.findItemBuyByItem(sellerItem);
            if (reviewedItem != null && reviewedItem.isReviewExists()) {
                reviewedSellerItemList.add(sellerItem);
            }
        }

        // 평균 매너점수 연산
        if(reviewedSellerItemList.size()==1){
            Double avgScore = (currentScore + reviewDTO.getScore()) / 2;
            seller.updateMannerScore(avgScore);
        } else{
            Double avgScore = ((currentScore*(reviewedSellerItemList.size())) + reviewDTO.getScore()) / (reviewedSellerItemList.size()+1);
            seller.updateMannerScore(avgScore);
        }
    }


}