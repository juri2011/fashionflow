package com.fashionflow.entity;

import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findMemberTest(){
        /* 1번 상품 갖고오기 */
        Item item = itemRepository.findById(1L).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + 1L));

        /* 1번 상품의 판매자 갖고오기 */
        Member member = memberRepository.findById(item.getMember().getId()).orElseThrow(() ->
                new EntityNotFoundException("회원이 존재하지 않습니다. id = " + item.getMember().getId()));

        System.out.println("========================================="+member);
        assertNotNull(member);
    }
}