package com.fashionflow.controller;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(Model model){

        List<ListingItemDTO> Top8products = itemService.getTop8productswithImg();

        model.addAttribute("Top8products", Top8products); // 리뷰가 이미 작성되었는지 여부를 모델에 추가

        return "main";

    }

    @GetMapping("/EmailVerify")
    public String EmailVerify(){
        return "EmailVerify";
    }

    @GetMapping("/ResetPwd")
    public String resetPassword() {
        return "ResetPwd";
    }
}