package com.fashionflow.controller;

import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.constant.ItemStatus;
import com.fashionflow.entity.Item;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ListingItemDTO>> listMoreProducts(@RequestParam("page") int currentPage, @RequestParam("size") int size,
                                                                 @RequestParam(value = "categories", required = false) String categories,
                                                                 @RequestParam(value = "saleStatus", required = false) String saleStatus,
                                                                 @RequestParam(value = "productCategories", required = false) String productCategories){
        Pageable pageable = PageRequest.of(currentPage, size); // 페이지 번호와 페이지당 항목 수 설정
        List<Long> categoryList = new ArrayList<>();
        if(categories != null && !categories.isEmpty()) {
            categoryList = Arrays.stream(categories.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        List<Long> saleStatusList = new ArrayList<>();
        if(saleStatus != null && !saleStatus.isEmpty()) {
            saleStatusList = Arrays.stream(saleStatus.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        List<Long> productCategoriesList = new ArrayList<>();
        if(productCategories != null && !productCategories.isEmpty()) {
            productCategoriesList = Arrays.stream(productCategories.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }


        Page<ListingItemDTO> goodsPage = listService.listingItemWithImgAndCategories(pageable, categoryList, saleStatusList, productCategoriesList);

        return ResponseEntity.ok(goodsPage.getContent()); // ResponseEntity를 통해 ListingItemDTO 목록을 JSON 형식으로 반환
    }
}
