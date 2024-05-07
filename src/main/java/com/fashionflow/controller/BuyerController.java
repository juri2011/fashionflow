package com.fashionflow.controller;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ReviewDTO;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.service.BuyerService;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    // 구매내역 페이지 이동
    @GetMapping("/orders")
    public String orders(Model model) {

        //구매자 구매내역 리스트
        List<BuyerDTO> getItemBuyListWithImg = buyerService.getItemBuyListWithImg();


        model.addAttribute("getItemBuyListWithImg", getItemBuyListWithImg); // 리뷰가 이미 작성되었는지 여부를 모델에 추가
        return "buyer/orders";
    }


    // 구매내역 리뷰
    @PostMapping("/reviews")
    public String reviews(@ModelAttribute ReviewDTO reviewDTO){

        buyerService.registerReview(reviewDTO);
        return "redirect:/buyer/orders";
    }
}
