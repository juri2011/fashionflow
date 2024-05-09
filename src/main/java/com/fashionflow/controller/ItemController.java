package com.fashionflow.controller;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.CategoryDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberDetailDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.dto.RecentViewItemDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import com.fashionflow.service.CategoryService;
import com.fashionflow.service.HeartService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.MemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@ControllerAdvice
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final HeartService heartService;

    private final MemberService memberService;

    private final CategoryService categoryService;

    private final ObjectMapper objectMapper;



    //상품 리스트 출력
    @GetMapping("/item/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId); //상품 상세정보(이미지, 태그, 카테고리 포함)
        Long heartCount = heartService.countHeartById(itemId); //상품 찜한 갯수
        MemberDetailDTO shopMember = memberService.getShopMember(itemFormDTO.getMemberId(), itemId);
        Member currentMember = memberService.findMemberByCurrentEmail();

        //조회수 증가
        itemService.updateViewCount(itemId);

        model.addAttribute("itemFormDTO", itemFormDTO);
        model.addAttribute("heartCount", heartCount);
        model.addAttribute("shopMember", shopMember);

        /*res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        res.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1.*/
        //model.addAttribute("currentMemberEmail", currentMemberEmail);

        // 최근 본 목록 불러오기
        List<RecentViewItemDTO> recentViewedItems = itemService.getRecentViewedItems(request);

        // 현재 상품이 이미 목록에 있는 경우 제거
        recentViewedItems.removeIf(item -> item.getItemId().equals(itemId));

        // 현재 상품을 최근 본 상품 목록의 맨 앞에 추가
        RecentViewItemDTO recentItem = itemService.getRecentView(itemId);
        recentViewedItems.add(0, recentItem);

        // 항목수 5개로 제한
        if (recentViewedItems.size() > 5) {
            recentViewedItems.remove(5);
        }

        // 업데이트된 최근 본 상품 목록을 JSON 문자열로 변환한 후, URL 인코딩을 적용
        String updatedRecentItemsJson = objectMapper.writeValueAsString(recentViewedItems);
        String encodedJson = URLEncoder.encode(updatedRecentItemsJson, "UTF-8");

        // 변환된 JSON 문자열을 쿠키에 저장
        Cookie recentItemsCookie = new Cookie("recentViewedItems", encodedJson);
        recentItemsCookie.setPath("/");
        recentItemsCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 1주
        response.addCookie(recentItemsCookie);
        model.addAttribute("recentViewedItems", recentViewedItems);


        return "item/itemDetail";
    }

    //service 패키지가 없으므로 임시로 repository 사용
    /*
    private final ItemRepository itemRepository;

    private final ItemTagRepository itemTagRepository;

    private final CategoryRepository categoryRepository;

    private final ItemImgRepository itemImgRepository;

    private final MemberRepository memberRepository;

    private final ProfileImageRepository profileRepository;

    private final EntityManager em;

    private final ItemSellRepository itemSellRepository;

    private final HeartRepository heartRepository;
    //상품 리스트 출력(구)

    @GetMapping("/item/orig/{itemId}")
    public String itemDetali(Model model, @PathVariable("itemId") Long itemId){
        *//* 원래는 서비스단에서 처리해야 할 코드 *//*

        
        *//* 상품 가져오기 *//*

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));


        *//* 상품 태그 가져오기 *//*
        List<ItemTag> itemTagList = itemTagRepository.findByItemId(item.getId()); //조회한 상품의 아이디로 상품 태그 찾기

        *//* item 객체 안에 저장된 카테고리 id를 이용해서 해당 카테고리 객체를 받아옴 *//*
        Category category = categoryRepository.findById(item.getCategory().getId())
                .orElseThrow(() ->
                new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + item.getCategory().getId()));

        
        *//* 상위 카테고리가 있다면 상위 카테고리 가져오기 *//*
        Category parentCategory = null;
        if(category.getParent() != null){
            parentCategory = categoryRepository.findById(category.getParent().getId())
                    .orElseThrow(() ->
                    new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + item.getCategory().getId()));
        }

        //System.out.println("=============================== 하위카테고리 : "+category);
        //System.out.println("=============================== 상위카테고리 : "+parentCategory);

        *//* 상품 이미지 목록 가져오기 *//*
        List<ItemImg> itemImgList = itemImgRepository.findByItemId(itemId);

        //itemImgList.forEach(itemImg -> System.out.println("======================== 파일명 : " + itemImg.getOriImgName()));

        *//* 판매자 정보 가져오기 *//*
        Member member = memberRepository.findById(item.getMember().getId())
                .orElseThrow(() ->
                new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + item.getMember().getId()));

        //System.out.println("============================" + member);

        *//* 사용자 프로필 사진 *//*
        ProfileImage profileImage = profileRepository.findByMemberId(member.getId());
        System.out.println(profileImage);

        *//* 현재 상품을 제외한 상품 정보 가져오기 *//*
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        List<Item> otherItemList = queryFactory.select(qItem)
                .from(qItem)
                .where(qItem.member.id.eq(member.getId()))
                .where(qItem.id.ne(itemId))
                .orderBy(qItem.id.desc())
                .fetch();

        //otherItemList.forEach(i -> System.out.println("======================== "+i));

        *//* 위의 상품 리스트의 대표사진 가져오기 *//*
        QItemImg qItemImg = QItemImg.itemImg;
        List<ItemImg> otherItemImgList = queryFactory.select(qItemImg)
                .from(qItemImg)
                .where(qItemImg.item.member.id.eq(member.getId()),
                        qItemImg.item.id.ne(itemId),
                        qItemImg.repimgYn.eq("Y"))
                .orderBy(qItemImg.item.regdate.desc())
                .limit(4)
                .fetch();
        //otherItemImgList.forEach(otherItemImg -> System.out.println("====================="+otherItemImg));

        *//* 판매수 *//*
        Long sellCount = itemSellRepository.countByMemberId(member.getId());

        System.out.println(sellCount);

        *//* 찜한 갯수 *//*
        Long heartCount = heartRepository.countByItemId(itemId);

        //얘네 FormDto로 묶어서 전달하는게 좋을듯
        model.addAttribute("item", item); //상품 전달
        model.addAttribute("itemTagList", itemTagList); //상품 태그 전달
        model.addAttribute("category", category); //하위 카테고리
        model.addAttribute("parentCategory", parentCategory); //상위 카테고리
        model.addAttribute("itemImgList", itemImgList); //상품 이미지 리스트
        //
        model.addAttribute("heartCount", heartCount); //찜한 갯수
        //
        model.addAttribute("member", member);//판매자 정보(주소나 비밀번호 등 민감한 정보가 들어가 있음)
        model.addAttribute("profileImage", profileImage);
        model.addAttribute("otherItemList", otherItemList); // 현재 상품을 제외한 다른 상품 리스트
        model.addAttribute("otherItemImgList", otherItemImgList); //판매자 다른 상품 이미지 리스트
        // column으로 따로 지정하지 않고 sellCount 테이블에서 사용자로 검색한 값을 출력
        model.addAttribute("sellCount", sellCount);//판매수



        return "item/itemDetail_orig";
    }*/



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
    public ResponseEntity<List<CategoryDTO>> getSubcategories(@PathVariable Long parentId) {
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
