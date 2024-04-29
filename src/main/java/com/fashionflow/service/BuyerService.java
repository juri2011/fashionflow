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

    public List<ItemBuy> getItemBuyList(){
        return itemBuyRepository.findAll();
    }
}