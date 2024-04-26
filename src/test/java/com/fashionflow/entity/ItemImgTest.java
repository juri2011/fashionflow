package com.fashionflow.entity;

import com.fashionflow.repository.ItemImgRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemImgTest {

    @Autowired
    ItemImgRepository itemImgRepository;

    @Test
    public void findItemImgTest(){

        List<ItemImg> itemImgList = itemImgRepository.findByItemId(1L); //1번 상품의 이미지 리스트 가져오기

        itemImgList.forEach(itemImg -> System.out.println("=============================== 검색결과 : "+itemImg));

    }

}