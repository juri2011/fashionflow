package com.fashionflow.service;

import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.dto.MemberDetailDTO;
import com.fashionflow.dto.ProfileImageDTO;
import com.fashionflow.entity.*;
import com.fashionflow.repository.ItemSellRepository;
import com.fashionflow.repository.MemberRepository;
import com.fashionflow.repository.ProfileImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemSellRepository itemSellRepository;

    @Test
    public void getItemImgTest(){
        /* 회원 상품 리스트 저장 */
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //현재 상품 번호만 제외하고 등록 순으로 판매자 다른 상품 조회
        List<Item> itemList = queryFactory.select(qItem)
                .from(qItem)
                .where(qItem.member.id.eq(1L))
                .where(qItem.id.ne(1L))
                .orderBy(qItem.id.desc())
                .limit(4)
                .fetch();
        List<ItemFormDTO> itemFormDTOList = new ArrayList<>();
        for(Item item : itemList){
            /* 이미지, 태그 정보는 들어가지 않음 (쓰이지 않음) */
            ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

            // 현재 상품의 대표이미지 검색
            QItemImg qItemImg = QItemImg.itemImg;
            ItemImg itemRepImg = queryFactory.select(qItemImg)
                    .from(qItemImg)
                    .where(qItemImg.item.id.eq(1L),
                            qItemImg.repimgYn.eq("Y"))
                    .fetchOne();
            ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

            itemFormDTO.setItemRepImgDTO(itemRepImgDTO);
            itemFormDTOList.add(itemFormDTO);
        }

        itemFormDTOList.forEach(itemFormDTO -> System.out.println("=================" + itemFormDTO));
    }

    public MemberDetailDTO getShopMember(Long memberId, Long itemId){

        /* 회원 프로필사진 저장 */
        ProfileImage profileImage = profileImageRepository.findByMemberId(memberId);
        ProfileImageDTO profileImageDTO = ProfileImageDTO.entityToDTO(profileImage);

        /* 회원 상품 리스트 저장 */
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //현재 상품 번호만 제외하고 등록 순으로 판매자 다른 상품 조회
        List<Item> itemList = queryFactory.select(qItem)
                .from(qItem)
                .where(qItem.member.id.eq(memberId))
                .where(qItem.id.ne(itemId))
                .orderBy(qItem.id.desc())
                .limit(4)
                .fetch();
        List<ItemFormDTO> itemFormDTOList = new ArrayList<>();
        for(Item item : itemList){
            /* 이미지, 태그 정보는 들어가지 않음 (쓰이지 않음) */
            ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

            // 현재 상품의 대표이미지 검색
            QItemImg qItemImg = QItemImg.itemImg;
            ItemImg itemRepImg = queryFactory.select(qItemImg)
                    .from(qItemImg)
                    .where(qItemImg.item.id.eq(itemId),
                            qItemImg.repimgYn.eq("Y"))
                    .fetchOne();
            ItemImgDTO itemRepImgDTO = ItemImgDTO.entityToDto(itemRepImg);

            itemFormDTO.setItemRepImgDTO(itemRepImgDTO);
        }
        /* 회원 정보 저장 */
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new EntityNotFoundException("해당 회원이 존재하지 않습니다. id = " + memberId));

        MemberDetailDTO memberDetailDTO = MemberDetailDTO.entityToDTOSafe(member);
        /* 프로필 사진 저장 */
        memberDetailDTO.setProfileImageDTO(profileImageDTO);
        /* 판매수 저장 */
        memberDetailDTO.setSellCount(itemSellRepository.countByMemberId(memberId));
        /* 판매자 다른 상품 목록 저장 */
        memberDetailDTO.setItemFormDTOList(itemFormDTOList);

        return memberDetailDTO;
    }
}