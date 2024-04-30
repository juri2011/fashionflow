package com.fashionflow.service;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.repository.ItemBuyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final ItemBuyRepository itemBuyRepository;

    private final MemberService memberService;

    public List<ItemBuy> getItemBuyList(){
        Long memberId = memberService.findMemberByCurrentEmail().getId();
        return itemBuyRepository.findByMemberId(memberId);
    }

}