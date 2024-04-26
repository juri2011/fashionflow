package com.fashionflow.controller;

import com.fashionflow.entity.Category;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.repository.CategoryRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.fashionflow.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    //service 패키지가 없으므로 임시로 repository 사용
    private final ItemRepository itemRepository;

    private final ItemTagRepository itemTagRepository;

    private final CategoryRepository categoryRepository;

    //상품 리스트 출력
    @GetMapping("/item/{itemId}")
    public String itemDetali(Model model, @PathVariable("itemId") Long itemId){
        /* 원래는 서비스단에서 처리해야 할 코드 */

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        List<ItemTag> itemTagList = itemTagRepository.findByItemId(item.getId()); //조회한 상품의 아이디로 상품 태그 찾기

        /* item 객체 안에 저장된 카테고리 id를 이용해서 해당 카테고리 객체를 받아옴 */
        Category category = categoryRepository.findById(item.getCategory().getId())
                .orElseThrow(() ->
                new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + item.getCategory().getId()));

        Category parentCategory = null;
        if(category.getParent() != null){
            parentCategory = categoryRepository.findById(category.getParent().getId())
                    .orElseThrow(() ->
                    new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + item.getCategory().getId()));
        }

        System.out.println("=============================== 하위카테고리 : "+category);
        System.out.println("=============================== 상위카테고리 : "+parentCategory);

        //얘네 FormDto로 묶어서 전달하는게 좋을듯
        model.addAttribute("item", item); //상품 전달
        model.addAttribute("itemTagList", itemTagList); //상품 태그 전달
        model.addAttribute("category", category); //하위 카테고리
        model.addAttribute("parentCategory", parentCategory); //상위 카테고리

        return "item/itemDetail";
    }
}
