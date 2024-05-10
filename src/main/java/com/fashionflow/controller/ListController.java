package com.fashionflow.controller;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListController {

    private final ItemRepository itemRepository;


    @GetMapping("/list")
    public String listPage(Model model){

        Pageable firstPageWithEightItems = PageRequest.of(0, 8); // 첫 페이지, 항목 8개
        model.addAttribute("productLists", itemRepository.findAll(firstPageWithEightItems).getContent());

        return "/list";
    }

    @GetMapping("/list/more")
    @ResponseBody
    public ResponseEntity<List<Item>> listMoreProducts(@RequestParam("page") int currentPage){
        Pageable pageable = PageRequest.of(currentPage, 1); // 페이지 번호와 페이지당 항목 수
        List<Item> items = itemRepository.findAll(pageable).getContent();

        return ResponseEntity.ok(items); // ResponseEntity를 통해 상품 목록을 JSON 형식으로 반환
    }
}
