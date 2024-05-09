package com.fashionflow.service;

import com.fashionflow.entity.Heart;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.HeartRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    private final ItemService itemService;

    public Long countHeartById(Long itemId){
        return heartRepository.countByItemId(itemId);
    }

    public String addHeart(Long itemId, String name) {
        //현재 상품
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));
        //현재 사용자
        Member member = memberRepository.findByEmail(name);
        //현재 로그인한 사용자의 찜 목록
        List<Heart> heartList = heartRepository.findByMember_Email(name);

        //이미 찜으로 등록되어 있는 경우 찜에서 삭제
        for(Heart heart : heartList){
            if(heart.getItem().equals(item)){
                heartRepository.delete(heart);
                return "찜에서 삭제되었습니다.";
            }
        }

        //찜으로 등록되어 있지 않으면 추가
        Heart heart = Heart.builder()
                .item(item)
                .member(member)
                .build();

        heartRepository.save(heart);
        return "찜 상품에 추가되었습니다.";
    }
}
