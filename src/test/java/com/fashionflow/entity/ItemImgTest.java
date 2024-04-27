package com.fashionflow.entity;

import com.fashionflow.repository.ItemImgRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemImgTest {

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    EntityManager em;

    @Test
    public void findItemImgTest(){

        List<ItemImg> itemImgList = itemImgRepository.findByItemId(1L); //1번 상품의 이미지 리스트 가져오기

        itemImgList.forEach(itemImg -> System.out.println("=============================== 검색결과 : "+itemImg));

    }
    @Test
    public void findItemImgExceptOneTest(){

        QItemImg qItemImg = QItemImg.itemImg;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<ItemImg> otherItemImgList = queryFactory.select(qItemImg)
                .from(qItemImg)
                .where(qItemImg.item.member.id.eq(1L),qItemImg.id.ne(1L),qItemImg.repimgYn.eq("Y"))
                .orderBy(qItemImg.id.desc())
                .fetch();
        /*
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qItemImg.item.member.id.eq(1L));
        booleanBuilder.and(qItemImg.item.id.ne(1L));
        booleanBuilder.and(qItemImg.repimgYn.eq("Y"));

        Iterable<ItemImg> otherItemImgIterable = itemImgRepository.findAll(booleanBuilder);
        List<ItemImg> otherItemImgList = new ArrayList<>();
        otherItemImgIterable.forEach(otherItemImgList::add);

         */
        otherItemImgList.forEach(System.out::println);

        /*
        for(ItemImg itemImg : otherItemImgList){
            System.out.println("===========================" + itemImg);
        }
        */
    }
}