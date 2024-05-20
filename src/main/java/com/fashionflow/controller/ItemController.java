package com.fashionflow.controller;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberDetailDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.RecentViewItemDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import com.fashionflow.service.CategoryService;
import com.fashionflow.service.HeartService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.MemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@ControllerAdvice
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final HeartService heartService;

    private final MemberService memberService;

    private final ObjectMapper objectMapper;



    //상품 리스트 출력
    @GetMapping("/item/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId); //상품 상세정보(이미지, 태그, 카테고리 포함)
        Long heartCount = heartService.countHeartById(itemId); //상품 찜한 갯수
        MemberDetailDTO shopMember = memberService.getShopMember(itemFormDTO.getMemberId(), itemId);
        String currentMember = memberService.currentMemberEmail();

        //조회수 증가
        itemService.updateViewCount(itemId);

        model.addAttribute("itemFormDTO", itemFormDTO);
        model.addAttribute("heartCount", heartCount);
        model.addAttribute("shopMember", shopMember);
        model.addAttribute("currentMember", currentMember);

        /*res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        res.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1.*/
        //model.addAttribute("currentMemberEmail", currentMemberEmail);

        // 최근 본 목록 불러오기
        List<RecentViewItemDTO> recentViewedItems = itemService.getRecentViewedItems(request);

        // 현재 상품이 이미 목록에 있는 경우 제거
        recentViewedItems.removeIf(item -> item.getItemId().equals(itemId));

        // 현재 상품을 최근 본 상품 목록의 맨 앞에 추가
        RecentViewItemDTO recentItem = itemService.getRecentView(itemId);
        recentViewedItems.add(0, recentItem);

        // 항목수 5개로 제한
        if (recentViewedItems.size() > 5) {
            recentViewedItems.remove(5);
        }

        // 업데이트된 최근 본 상품 목록을 JSON 문자열로 변환한 후, URL 인코딩을 적용
        String updatedRecentItemsJson = objectMapper.writeValueAsString(recentViewedItems);
        String encodedJson = URLEncoder.encode(updatedRecentItemsJson, "UTF-8");

        // 변환된 JSON 문자열을 쿠키에 저장
        Cookie recentItemsCookie = new Cookie("recentViewedItems", encodedJson);
        recentItemsCookie.setPath("/");
        recentItemsCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 1주
        response.addCookie(recentItemsCookie);
        model.addAttribute("recentViewedItems", recentViewedItems);


        return "item/itemDetail";
    }

}
