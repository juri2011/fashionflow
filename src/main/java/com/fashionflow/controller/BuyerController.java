package com.fashionflow.controller;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.service.BuyerService;
import com.fashionflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<ItemBuy> purchaseList = buyerService.getItemBuyList();
        model.addAttribute("purchaseList", purchaseList);
        return "buyer/orders";
    }
}
