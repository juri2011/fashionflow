package com.fashionflow.controller;

import com.fashionflow.dto.HeartDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.service.HeartService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.MemberService;
import com.fashionflow.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
@ActiveProfiles("test")
public class ShopControllerTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private HeartService heartService;

    @Mock
    private MemberService memberService; // Mock MemberService 추가

    @InjectMocks
    private ShopController shopController;

    @Test
    public void testShowMyShop() {
        // Given
        User userDetails = mock(User.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // MemberService를 모의(Mock)하고 currentMemberEmail() 메서드의 동작을 지정하여 null이 아닌 이메일 주소를 반환하도록 함
        when(memberService.currentMemberEmail()).thenReturn("test@example.com");

        // 회원 정보 생성
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@example.com");
        member.setName("Test User");

        // 더미 데이터 생성 - 상품 목록
        List<ItemFormDTO> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemFormDTO item = new ItemFormDTO();
            item.setId((long) (i + 1));
            item.setItemName("Item " + (i + 1));
            item.setPrice(10000 + (i * 1000));
            items.add(item);
        }

        // 더미 데이터 생성 - 리뷰 목록
        List<ReviewDTO> reviewList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ReviewDTO review = new ReviewDTO();
            review.setId((long) (i + 1));
            review.setItemName("Item " + (i + 1));
            review.setContent("Review content for Item " + (i + 1));
            reviewList.add(review);
        }

        // 더미 데이터 생성 - 좋아요한 상품 목록
        List<HeartDTO> heartItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HeartDTO heartItem = new HeartDTO();
            heartItem.setId((long) (i + 1));
            heartItem.setItemName("Item " + (i + 1));
            heartItem.setPrice(10000 + (i * 1000));
            heartItems.add(heartItem);
        }

        // memberRepository.findByEmail 메서드가 호출될 때 member를 반환하도록 설정
        when(memberRepository.findByEmail(anyString())).thenReturn(member);
        // itemService.getItemsWithImagesByUserEmail 메서드가 호출될 때 items를 반환하도록 설정
        when(itemService.getItemsWithImagesByUserEmail(anyString())).thenReturn(items);
        // reviewService.getItemReviewListWithImg 메서드가 호출될 때 reviewList를 반환하도록 설정
        when(reviewService.getItemReviewListWithImg(anyString())).thenReturn(reviewList);
        // heartService.getHeartItemsWithImagesByUserEmail 메서드가 호출될 때 heartItems를 반환하도록 설정
        when(heartService.getHeartItemsWithImagesByUserEmail(anyString())).thenReturn(heartItems);

        // Mock Model 객체 생성
        Model model = mock(Model.class);

        // When
        String viewName = shopController.showMyShop(userDetails, model);

        // Then
        assertEquals("myshop", viewName);
        // 모델에 상품 목록, 리뷰 목록, 좋아요한 상품 목록이 올바르게 추가되었는지 확인
        verify(model).addAttribute(eq("items"), anyList());
        verify(model).addAttribute(eq("getItemReviewListWithImg"), anyList());
        verify(model).addAttribute(eq("getHeartItemsWithImagesByUserEmail"), anyList());
    }

}
