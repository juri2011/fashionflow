package com.fashionflow.service;

import com.fashionflow.dto.HeartDTO;
import com.fashionflow.entity.Heart;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.HeartRepository;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;


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

    public List<HeartDTO> getHeartItemsWithImagesByUserEmail(String userEmail) {
        List<HeartDTO> heartDTOs = new ArrayList<>();

        // 사용자의 찜 목록을 가져옴
        List<Heart> hearts = heartRepository.findByMember_Email(userEmail);

        for (Heart heart : hearts) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(heart.getItem().getId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            // 이미지가 존재하면 해당 경로를 사용하고, 그렇지 않으면 기본 이미지 경로를 사용합니다.
            String imgName = img != null ? "/images/"+img.getImgName() : "/img/default.PNG";

            HeartDTO dto = new HeartDTO();
            dto.setId(heart.getItem().getId());
            dto.setItemName(heart.getItem().getItemName());
            dto.setPrice(heart.getItem().getPrice());
            dto.setImgName(imgName);
            heartDTOs.add(dto);
        }

        return heartDTOs;
    }


    public String removeHeart(Long itemId, String userEmail) {
        // 사용자 이메일과 상품 ID로 찜 항목을 찾음
        Heart heart = heartRepository.findByMember_EmailAndItem_Id(userEmail, itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 찜이 존재하지 않습니다. 사용자 이메일: " + userEmail + ", 상품 ID: " + itemId));

        // 찜 삭제
        heartRepository.delete(heart);

        // 삭제 후 /myshop 페이지로 리다이렉트
        return "redirect:/myshop";
    }
}
