package com.fashionflow.entity;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemTagTest {

    @Autowired
    ItemTagRepository itemTagRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    //아이디로 상품 찾기
    private Item findItem(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        return item;
    }

    //상품에 태그 생성하기
    private List<ItemTag> createItemTag(Item item, List<ItemTagName> ItemTagNameList){


        List<ItemTag> itemTagList = new ArrayList<>(); //상품 하나에 들어갈 태그 목록

        //사용자가 추가한 태그를 순차적으로 조회하여 DB에 삽입
        for(ItemTagName tag : ItemTagNameList){
            ItemTag itemTag = ItemTag.builder()
                    .itemTagName(tag)
                    .item(item)
                    .build();

            itemTagList.add(itemTag);
        }

        return itemTagList;
    }

    @Test
    @Commit
    public void createItemTagTest(){
        Item item = findItem(1L); //1번 상품 찾기

        /* 임의로 들어갈 태그 목록 지정 */
        List<ItemTagName> itemTagNameList = new ArrayList<>(); //사용자가 입력한 태그 목록
        itemTagNameList.add(ItemTagName.DIRECT_TRADE); //직거래 가능
        itemTagNameList.add(ItemTagName.DELIVERY_TRADE); //택배거래 가능

        /* 사용자가 입력한 태그 목록을 이용해서 DB에 들어갈 태그 데이터 생성 */
        List<ItemTag> itemTagList = createItemTag(item, itemTagNameList);

        /* 생성한 태그 데이터를 실제 DB에 저장 */
        List<ItemTag> savedItemTagList = itemTagRepository.saveAll(itemTagList);

        savedItemTagList.forEach(savedItemTag -> System.out.println("=========================" + savedItemTag));

    }

}