package com.fashionflow.controller;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.*;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.service.BuyerService;
import com.fashionflow.service.CategoryService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShopController {


    private final ItemService itemService;

    private final ItemRepository itemRepository;

    private final CategoryService categoryService;

    private final ReviewService reviewService;


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
                           @RequestParam("tagSelectList") List<String> tagSelectList, // 태그 선택 정보 추가
                           Principal principal, Model model) {


        if (bindingResult.hasErrors()) {
            return "/item/itemForm";  // 입력 폼으로 리턴
        }

        try {
            String email = principal.getName(); // 사용자 이메일 가져오기
            itemService.saveItem(itemFormDTO, itemImgFileList, tagSelectList, email); // 태그 정보도 함께 저장
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 실패");
            return "/item/itemForm";
        }
    }

    @GetMapping("/myshop")
    public String showMyShop(@AuthenticationPrincipal User user, Model model) {
        String userEmail = user.getUsername(); // 현재 로그인한 사용자의 이메일을 가져옴
        List<ItemFormDTO> items = itemService.getItemsWithImagesByUserEmail(userEmail);
        // 리뷰내역 리스트
        List<ReviewDTO> getItemReviewListWithImg = reviewService.getItemReviewListWithImg();


        model.addAttribute("getItemReviewListWithImg", getItemReviewListWithImg); // 리뷰가 이미 작성되었는지 여부를 모델에 추가
        model.addAttribute("items", items);
        return "myshop"; // HTML 파일 이름과 일치
    }


    @GetMapping("/members/item/{itemId}")
    public String modifyItemForm(@PathVariable("itemId") Long itemId, Model model) {
        // 상품 상세 정보 가져오기
        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId);
        model.addAttribute("itemFormDTO", itemFormDTO);

        // 상위 카테고리 정보 가져오기
        List<CategoryDTO> parentCategories = categoryService.findParentCategories();
        model.addAttribute("categories", parentCategories);




        return "item/itemModifyForm";
    }

    @PostMapping("/members/item/{itemId}")
    public String updateItem(@PathVariable("itemId") Long itemId,
                             @ModelAttribute("itemFormDTO") ItemFormDTO itemFormDTO,
                             RedirectAttributes redirectAttributes) {
        if (itemId == null) {
            // 아이템 ID가 null인 경우 처리
            redirectAttributes.addFlashAttribute("errorMessage", "상품 ID가 null입니다.");
            return "redirect:/myshop";
        }

        try {
            itemService.updateItem(itemId, itemFormDTO);
            return "redirect:/myshop";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addAttribute("itemId", itemId);
            return "forward:/members/item/{itemId}";
        }
    }

    @PostMapping("/members/item/delete/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") Long itemId) {
        try {
            // 상품을 삭제합니다.
            itemService.deleteItem(itemId);
            // 상품 삭제에 성공하면 200 OK 응답을 반환합니다.
            return ResponseEntity.ok("Item deleted successfully.");
        } catch (Exception e) {
            // 상품 삭제에 실패하면 500 Internal Server Error 응답을 반환합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete item.");
        }
    }



}
