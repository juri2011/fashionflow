package com.fashionflow.service;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ItemImgDTO;
import com.fashionflow.dto.ItemTagDTO;
import com.fashionflow.dto.RecentViewItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemTag;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import com.fashionflow.repository.ItemTagRepository;
import com.fashionflow.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    private final ItemTagRepository itemTagRepository;

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public void saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList, String tagSelect, String userEmail) throws Exception {
        Member member = memberRepository.findByEmail(userEmail);
        if (member == null) {
            throw new RuntimeException("회원 정보를 찾을 수 없습니다.");
        }

        Item item = itemFormDTO.createItem();
        item.setMember(member);
        itemRepository.save(item); // 상품 저장

        // 이미지 파일 처리
        if (itemImgFileList != null && !itemImgFileList.isEmpty()) {
            for (int i = 0; i < itemImgFileList.size(); i++) {
                MultipartFile file = itemImgFileList.get(i);
                if (!file.isEmpty()) {
                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    itemImg.setRepimgYn(i == 0 ? "Y" : "N"); // 첫 번째 이미지를 대표 이미지로 설정
                    itemImgService.saveItemImg(itemImg, file);
                }
            }
        }

        // 태그 저장
        try {
            if (tagSelect != null && !tagSelect.isEmpty()) {
                ItemTag itemTag = new ItemTag();
                itemTag.setItem(item);
                itemTag.setItemTagName(ItemTagName.valueOf(tagSelect));
                itemTagRepository.save(itemTag);
            }
        } catch (IllegalArgumentException e) {
            // 로그 출력 및 적절한 예외 처리
            System.out.println("잘못된 태그 이름이 입력되었습니다: " + tagSelect);
            // 예외 처리 로직, 필요한 경우 사용자에게 메시지 전달 등
        }
    }


    /* 상품 상세정보 + 이미지 가져오기 */

    public List<ItemImgDTO> getItemImgDTOList(Long itemId){
        /* 상품 이미지 리스트 가져오기 */
        //상품 이미지 번호 순으로 상품이미지 리스트 가져오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        //ItemFormDTO에 저장할 ItemImgDTO 리스트에 위의 데이터 하나씩 저장(데이터 형 변환)
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){
            //ItemImg엔티티 하나씩 가져와서 ItemImgDTO 형으로 변환
            ItemImgDTO itemImgDTO = ItemImgDTO.entityToDto(itemImg);
            itemImgDTOList.add(itemImgDTO);
        }

        return itemImgDTOList;
    }

    public List<ItemTagDTO> getItemTagDTOList (Long itemId){
        /* 상품 태그 리스트 가져오기 */
        List<ItemTag> itemTagList = itemTagRepository.findByItemId(itemId);
        List<ItemTagDTO> itemTagDTOList = new ArrayList<>();
        for(ItemTag itemTag : itemTagList){
            ItemTagDTO itemTagDTO = ItemTagDTO.entityToDTO(itemTag);
            itemTagDTOList.add(itemTagDTO);
        }

        return itemTagDTOList;
    }


    /* 상품 상세정보 + 이미지 가져오기 */
    public ItemFormDTO getItemDetail(Long itemId){

        //Repository에서 파라미터(상품 번호)로 Item 엔티티 가져오기
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        //Item 객체를 ItemFormDTO로 형변환
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);

        //위에서 변환시킨 ItemImgDTOList를 itemFormDTO 객체로 가져오기
        itemFormDTO.setItemImgDTOList(getItemImgDTOList(itemId));
        itemFormDTO.saveRepimg();

        itemFormDTO.setItemTagDTOList(getItemTagDTOList(itemId));

        return itemFormDTO;

    }

    public void updateViewCount(Long itemId){
        //Repository에서 파라미터(상품 번호)로 Item 엔티티 가져오기
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));
        System.out.println("조회수 : "+item.getViewCount());
        int viewCount = item.getViewCount();
        item.setViewCount(viewCount+1);
        itemRepository.save(item);
    }


    // 최근 본 아이템 정보 DTO 추가
    public RecentViewItemDTO getRecentView(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + itemId));

        ItemImg repImg = itemImgRepository.findFirstByItemIdAndRepimgYn(itemId, "Y")
                .orElse(null); // 대표 이미지 조회

        RecentViewItemDTO recentViewItemDTO = new RecentViewItemDTO();
        recentViewItemDTO.setItemId(item.getId());
        recentViewItemDTO.setItemName(item.getItemName());
        if (repImg != null) {
            recentViewItemDTO.setImgName(repImg.getImgName());
        }

        return recentViewItemDTO;
    }

    // 쿠키에서 최근 본 상품 목록 불러오기
    public List<RecentViewItemDTO> getRecentViewedItems(HttpServletRequest request) throws IOException {

        //모든 쿠키 가져오기
        Cookie[] cookies = request.getCookies();
        String recentViewedItemsJson = null;

        // "recentViewedItems" 쿠키 조회
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 존재할시 JSON URL 디코딩
                if ("recentViewedItems".equals(cookie.getName())) {
                    recentViewedItemsJson = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }
        }

        List<RecentViewItemDTO> recentViewedItems;
        // JSON 문자열 null이 아니라면, RecentViewItemDTO 객체 리스트로 변환
        if (recentViewedItemsJson != null) {
            recentViewedItems = objectMapper.readValue(recentViewedItemsJson, new TypeReference<List<RecentViewItemDTO>>() {});
        } else {
            // 또는 빈 리스트를 생성
            recentViewedItems = new ArrayList<>();
        }
        return recentViewedItems;
    }

}
