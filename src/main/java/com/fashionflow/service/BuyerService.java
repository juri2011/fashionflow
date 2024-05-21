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

    private final ItemBuyRepository itemBuyRepository; // 아이템 구매 리포지토리
    private final ReviewRepository reviewRepository; // 리뷰 리포지토리
    private final ItemRepository itemRepository; // 아이템 리포지토리
    private final MemberService memberService; // 회원 서비스
    private final ItemImgRepository itemImgRepository; // 아이템 이미지 리포지토리
    private final MemberRepository memberRepository; // 회원 리포지토리
    private final ReviewTagRepository reviewTagRepository; // 리뷰 태그 리포지토리

    // 구매한 아이템 리스트를 가져오는 메소드
    public Page<BuyerDTO> getItemBuyListWithImg(Pageable pageable) {

        Long memberId = memberService.findMemberByCurrentEmail().getId(); // 현재 이메일에 해당하는 회원 아이디 가져오기
        Page<ItemBuy> itemBuyPage = itemBuyRepository.findByMemberId(memberId, pageable); // 회원 아이디로 구매한 아이템 페이지 가져오기

        List<BuyerDTO> buyerDTO = itemBuyPage.getContent().stream().map(itemBuy -> {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(itemBuy.getItem().getId(), "Y") // 대표 이미지 가져오기
                    .orElse(null);
            String imgName = img != null ? "/images/" + img.getImgName() : "/img/default.PNG"; // 이미지 이름 설정
            return new BuyerDTO(itemBuy.getItem().getId(), itemBuy.getItem().getItemName(),
                    itemBuy.getItem().getPrice(), imgName, itemBuy.getBuyDate(), itemBuy.isReviewExists());
        }).collect(Collectors.toList());

        return new PageImpl<>(buyerDTO, pageable, itemBuyPage.getTotalElements()); // 구매자 DTO 페이지 반환
    }


    @Transactional // 리뷰 등록 메소드 트랜잭션
    public void registerReview(ReviewDTO reviewDTO){

        Member member = memberService.findMemberByCurrentEmail(); // 현재 이메일로 회원 가져오기
        Item item = itemRepository.findItemById(reviewDTO.getId()); // 아이템 ID로 아이템 가져오기

        // 리뷰 저장
        Review review = Review.builder()
                .itemId(item != null ? item.getId() : null)
                .memberEmail(member != null ? member.getEmail() : null)
                .sellerEmail(item != null ? item.getMember().getEmail() : null)
                .content(reviewDTO.getContent())
                .score(reviewDTO.getScore())
                .regDate(LocalDateTime.now())
                .build();

        review = reviewRepository.save(review); // 리뷰 저장

        // 리뷰 태그 리스트 가져오기
        List<ReviewTagContent> reviewTags = reviewDTO.getReviewTags();

        if (reviewTags != null && !reviewTags.isEmpty()) {
            for (ReviewTagContent tagContent : reviewTags) {
                ReviewTag reviewTag = new ReviewTag();
                reviewTag.setReview(review); // 저장된 리뷰 객체 설정
                reviewTag.setReviewTagContent(tagContent); // 태그 내용 설정

                reviewTagRepository.save(reviewTag); // 리뷰 태그 저장
            }
        }

        // 아이템 리뷰 여부 변경
        ItemBuy itemBuy = itemBuyRepository.findItemBuyByItem(item);
        if (itemBuy != null) {
            itemBuy.setReviewExists(true);
            itemBuyRepository.save(itemBuy); // 아이템 리뷰 여부 저장
        }

        // 판매자의 매너점수 업데이트
        Member seller = item.getMember(); // 아이템 판매자 가져오기
        // 모든 판매자 아이템 리스트 가져오기
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