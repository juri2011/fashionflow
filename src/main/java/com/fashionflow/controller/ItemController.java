package com.fashionflow.controller;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.MemberDetailDTO;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import com.fashionflow.service.HeartService;
import com.fashionflow.service.ItemService;
import com.fashionflow.service.MemberService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final HeartService heartService;

    private final MemberService memberService;

    //상품 리스트 출력
    @GetMapping("/item/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") Long itemId){

        ItemFormDTO itemFormDTO = itemService.getItemDetail(itemId); //상품 상세정보(이미지, 태그, 카테고리 포함)
        Long heartCount = heartService.countHeartById(itemId); //상품 찜한 갯수
        //System.out.println("=====================" + itemFormDTO);
        MemberDetailDTO shopMember = memberService.getShopMember(itemFormDTO.getMemberId(), itemId);
        System.out.println("==========================" + shopMember);

        model.addAttribute("itemFormDTO", itemFormDTO);
        model.addAttribute("heartCount", heartCount);
        model.addAttribute("shopMember", shopMember);
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
}
