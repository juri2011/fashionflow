package com.fashionflow.controller;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.service.CategoryService;
import com.fashionflow.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {


    private final ItemService itemService;

    private final CategoryService categoryService;


    @GetMapping("/members/item/new")
    public String itemForm(Model model) {
        // 새 ItemFormDTO 객체를 모델에 추가
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        // 카테고리 서비스를 사용하여 상위 카테고리만을 DTO 리스트로 가져오고 모델에 추가
        List<CategoryDTO> parentCategories = categoryService.findParentCategories();
        model.addAttribute("categories", parentCategories);

        // 태그선택 옵션을 동적으로 생성
        model.addAttribute("allTags", ItemTagName.values());

        // 상품 등록 폼 뷰 이름 반환
        return "item/itemForm";
    }


    @GetMapping("/getSubcategories/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getSubcategories(@PathVariable("parentId") Long parentId) {
        List<CategoryDTO> subcategories = categoryService.findSubcategoriesByParentId(parentId);
        return ResponseEntity.ok(subcategories);
    }


    @PostMapping("/members/item/new")
    public String saveItem(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                           @RequestParam("tagSelect") String tagSelect, // 태그 선택 정보 추가
                           Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "/item/itemForm";  // 입력 폼으로 리턴
        }

        try {
            String email = principal.getName(); // 사용자 이메일 가져오기
            itemService.saveItem(itemFormDTO, itemImgFileList, tagSelect, email); // 태그 정보도 함께 저장
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 실패");
            return "/item/itemForm";
        }
    }


}
