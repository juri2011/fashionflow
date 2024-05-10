package com.fashionflow.controller;

import com.fashionflow.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ListController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    public String listPage(Model model){

        model.addAttribute("productLists",itemRepository.findAll());

        return "/list";
    }
}
