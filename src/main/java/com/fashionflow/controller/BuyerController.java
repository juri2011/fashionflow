package com.fashionflow.controller;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.service.BuyerService;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;
    private final MemberService memberService;

    // 구매내역 페이지 이동
    @GetMapping("/orders")
    public String orders(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        if(memberService.findMemberByCurrentEmail()==null){
            return "redirect:/";
        }

        int pageSize = 5;

        //구매자 구매내역 리스트
        Page<BuyerDTO> getItemBuyListWithImg = buyerService.getItemBuyListWithImg(PageRequest.of(page, pageSize));


        model.addAttribute("getItemBuyListWithImg", getItemBuyListWithImg);
        return "buyer/orders";
    }


    // 구매내역 리뷰
    @PostMapping("/reviews")
    public String reviews(@ModelAttribute ReviewDTO reviewDTO){

        buyerService.registerReview(reviewDTO);
        return "redirect:/buyer/orders";
    }
}
