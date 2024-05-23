package com.fashionflow.controller;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.*;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Review;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.service.*;
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

    private final MemberService memberService;

    private final ItemService itemService;

    private final CategoryService categoryService;

    private final ReviewService reviewService;

    private final HeartService heartService;

    // 상품 등록 폼을 보여주는 메서드
    @GetMapping("/members/item/new")
    public String itemForm(@AuthenticationPrincipal User user, Model model) {
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        // 현재 로그인된 사용자의 정보 가져오기
        String userEmail = memberService.currentMemberEmail();
        // 비회원인 경우 로그인 페이지로 리디렉션
        if (userEmail.equals("anonymousUser")) {
            return "error/loginError";
        }

        // 새 ItemFormDTO 객체를 모델에 추가
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        // 카테고리 서비스를 사용하여 상위 카테고리만을 DTO 리스트로 가져오고 모델에 추가
        List<CategoryDTO> parentCategories = categoryService.findParentCategories();
        model.addAttribute("categories", parentCategories);

        // 태그 선택 옵션을 동적으로 생성
        model.addAttribute("allTags", ItemTagName.values());

        // 상품 등록 폼 뷰 이름 반환
        return "item/itemForm";
    }

    // 상위 카테고리에 따른 하위 카테고리를 가져오는 메서드
    @GetMapping("/getSubcategories/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getSubcategories(@PathVariable("parentId") Long parentId) {
        List<CategoryDTO> subcategories = categoryService.findSubcategoriesByParentId(parentId);
        return ResponseEntity.ok(subcategories);
    }

    // 새로운 상품을 저장하는 메서드
    @PostMapping("/members/item/new")
    public String saveItem(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                           @RequestParam(value="tagSelectList", required = false) List<String> tagSelectList,
                           Model model) {

        // 현재 로그인된 사용자의 이메일 가져오기
        String userEmail = memberService.currentMemberEmail();

        if (userEmail.equals("anonymousUser")) {
            return "error/loginError";
        }

        if (bindingResult.hasErrors()) {
            return "item/itemForm";  // 입력 폼으로 리턴
        }

        try {
            // 상품 저장
            itemService.saveItem(itemFormDTO, itemImgFileList, tagSelectList, userEmail); // 태그 정보도 함께 저장
            return "redirect:/myshop";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 실패");
            return "item/itemForm";
        }
    }

    // 사용자의 상점 페이지를 보여주는 메서드
    @GetMapping("/myshop")
    public String showMyShop(@AuthenticationPrincipal User user, Model model) {
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        // 현재 로그인된 사용자의 이메일 가져오기
        String userEmail = memberService.currentMemberEmail();

        if (userEmail.equals("anonymousUser")) {
            return "error/loginError";
        }

        // 현재 사용자의 상품 목록, 리뷰 목록, 좋아요한 상품 목록 가져오기
        List<ItemFormDTO> items = itemService.getItemsWithImagesByUserEmail(userEmail);
        List<ReviewDTO> getItemReviewListWithImg = reviewService.getItemReviewListWithImg(userEmail);
        List<HeartDTO> getHeartItemsWithImagesByUserEmail = heartService.getHeartItemsWithImagesByUserEmail(userEmail);

        // 모델에 데이터 추가
        model.addAttribute("items", items);
        model.addAttribute("getItemReviewListWithImg", getItemReviewListWithImg);
        model.addAttribute("getHeartItemsWithImagesByUserEmail", getHeartItemsWithImagesByUserEmail);

        // myshop.html 페이지로 이동
        return "myshop";
    }

    // 상품 수정 폼을 보여주는 메서드
    @GetMapping("/members/item/{itemId}")
    public String modifyItemForm(@PathVariable("itemId") Long itemId, Model model) {
        if(memberService.findUnregisteredOAuthMember()){
            return "redirect:/oauth/login";
        }
        // 상품 상세 정보 가져오기
        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId);
        model.addAttribute("itemFormDTO", itemFormDTO);

        // 상위 카테고리 정보 가져오기
        List<CategoryDTO> parentCategories = categoryService.findParentCategories();
        model.addAttribute("categories", parentCategories);

        return "item/itemModifyForm";
    }

    // 상품 정보를 업데이트하는 메서드
    @PostMapping("/members/item/{itemId}")
    public String updateItem(@PathVariable("itemId") Long itemId,
                             @ModelAttribute("itemFormDTO") ItemFormDTO itemFormDTO,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             @RequestParam(value = "itemImgIds", required = false) List<String> itemImgIdStringList,
                             @RequestParam(value = "tagSelectList", required = false) List<String> tagSelectList,
                             RedirectAttributes redirectAttributes) {
        for(String tagSelect : tagSelectList){
            ItemTagDTO itemTagDTO = new ItemTagDTO();
            itemTagDTO.setItemTagName(ItemTagName.valueOf(tagSelect));

            itemFormDTO.getItemTagDTOList().add(itemTagDTO);
        }

        if (itemId == null) {
            // 아이템 ID가 null인 경우 처리
            redirectAttributes.addFlashAttribute("errorMessage", "상품 ID가 null입니다.");

            return "redirect:/myshop";
        }

        try {
            itemService.updateItem(itemId, itemFormDTO, itemImgFileList, itemImgIdStringList);
            return "redirect:/myshop";
        } catch (Exception e) {
            redirectAttributes.addAttribute("itemId", itemId);
            return "forward:/members/item/{itemId}";
        }
    }

    // 상품을 삭제하는 메서드
    @PostMapping("/members/item/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId, RedirectAttributes redirectAttributes) {
        try {
            // 상품을 삭제합니다.
            itemService.deleteItem(itemId);
            // 상품 삭제에 성공하면 "해당 상품이 삭제 되었습니다" 메시지를 포함한 리다이렉트를 반환합니다.
            redirectAttributes.addFlashAttribute("successMessage", "해당 상품이 삭제 되었습니다.");
            return "redirect:/myshop";
        } catch (Exception e) {
            // 상품 삭제에 실패하면 "상품 삭제에 실패했습니다" 메시지를 포함한 리다이렉트를 반환합니다.
            redirectAttributes.addFlashAttribute("errorMessage", "상품 삭제에 실패했습니다.");
            return "redirect:/myshop";
        }
    }
}
