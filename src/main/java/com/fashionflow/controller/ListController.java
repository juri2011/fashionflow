package com.fashionflow.controller;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.fashionflow.service.ListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @GetMapping("/list")
    public String listPage() {
        return "/list";
    }
    @GetMapping("/list/more")
    @ResponseBody
    public ResponseEntity<List<ListingItemDTO>> listMoreProducts(@RequestParam("page") int currentPage, @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(currentPage, size); // 페이지 번호와 페이지당 항목 수 설정
        Page<ListingItemDTO> goodsPage = listService.listingItemWithImg(pageable);

        return ResponseEntity.ok(goodsPage.getContent()); // ResponseEntity를 통해 ListingItemDTO 목록을 JSON 형식으로 반환
    }
}
