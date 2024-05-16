package com.fashionflow.service;

import com.fashionflow.constant.ItemTagName;
import com.fashionflow.dto.*;
import com.fashionflow.entity.*;
import com.fashionflow.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final CategoryRepository categoryRepository;

    private final ItemImgRepository itemImgRepository;

    private final ItemTagRepository itemTagRepository;

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public void saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList, List<String> tagSelect, String userEmail) throws Exception {
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
        if(tagSelect != null && !tagSelect.isEmpty()){
            for(String tag : tagSelect){
                try {
                    ItemTag itemTag = new ItemTag();
                    itemTag.setItem(item);
                    itemTag.setItemTagName(ItemTagName.valueOf(tag));
                    itemTagRepository.save(itemTag);
                } catch (IllegalArgumentException e) {
                    // 로그 출력 및 적절한 예외 처리
                    System.out.println("잘못된 태그 이름이 입력되었습니다: " + tag);
                    // 예외 처리 로직, 필요한 경우 사용자에게 메시지 전달 등
                }
            }
        }


    }

    // 상품관리 정보 가져오기
    @Transactional(readOnly = true)
    public List<ItemFormDTO> getItemsWithImagesByUserEmail(String email) {
        // 회원을 이메일로 조회
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        // 회원이 등록한 상품 목록 조회 (최신 등록 순)
        List<Item> items = itemRepository.findByMemberOrderByRegdateDesc(member);

        // 상품 DTO와 각 상품의 대표 이미지 정보를 저장할 리스트 생성
        List<ItemFormDTO> itemFormDTOs = new ArrayList<>();
        for (Item item : items) {
            ItemFormDTO dto = ItemFormDTO.of(item);

            // 해당 상품의 대표 이미지 조회
            ItemImg repImg = itemImgRepository.findFirstByItemIdAndRepimgYn(item.getId(), "Y").orElse(null);
            if (repImg != null) {
                List<ItemImgDTO> imgDTOList = new ArrayList<>();
                imgDTOList.add(ItemImgDTO.entityToDto(repImg));
                dto.setItemImgDTOList(imgDTOList); // DTO에 이미지 정보 설정
            }

            itemFormDTOs.add(dto);
        }

        return itemFormDTOs;
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

    /**
     * 해당 상품의 카테고리 정보를 가져옵니다.
     * @param itemId 상품 ID
     * @return 카테고리 DTO 리스트
     */
    public List<CategoryDTO> getItemCategories(Long itemId) {
        // 상품의 카테고리 ID를 사용하여 해당 상품의 카테고리 정보를 가져옵니다.
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        // 카테고리 정보 가져오기
        Category category = item.getCategory();

        // 하위 카테고리 정보 가져오기
        List<Category> subcategories = categoryRepository.findByParentId(category.getId());

        // 카테고리 DTO 리스트로 변환하여 반환
        return subcategories.stream()
                .map(CategoryDTO::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * 해당 상품의 태그 정보를 가져옵니다.
     * @param itemId 상품 ID
     * @return 태그 DTO 리스트
     */
    public List<ItemTagDTO> getItemTags(Long itemId) {
        // 상품의 태그 정보를 가져옵니다.
        List<ItemTag> itemTags = itemTagRepository.findByItemId(itemId);

        // 태그 DTO 리스트로 변환하여 반환
        return itemTags.stream()
                .map(ItemTagDTO::entityToDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public void updateItem(Long itemId, ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
        // Repository에서 상품 번호를 사용하여 Item 엔티티 가져오기
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

        // ItemFormDTO에서 받은 정보를 사용하여 Item 엔티티 업데이트
        item.updateItemInfo(itemFormDTO.getItemName(), categoryRepository.findById(itemFormDTO.getCategoryDTO().getId())
                        .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다. id = " + itemFormDTO.getCategoryDTO().getId())),
                itemFormDTO.getSellStatus(), itemFormDTO.getItemStatus(), itemFormDTO.getContent(),
                itemFormDTO.getPrice(), itemFormDTO.getDelivery(), itemFormDTO.getAddress());

        // 이미지 파일 처리
        if (itemImgFileList != null && !itemImgFileList.isEmpty()) {
            // 이전에 등록된 상품 이미지 삭제
            itemImgRepository.deleteByItemId(itemId);
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

        // ItemTagDTOList 업데이트
        List<ItemTagDTO> itemTagDTOList = itemFormDTO.getItemTagDTOList();
        if (itemTagDTOList != null && !itemTagDTOList.isEmpty()) {
            // 이전에 등록된 상품 태그 삭제
            itemTagRepository.deleteByItemId(itemId);
            // 새로운 상품 태그 등록
            List<ItemTag> itemTags = itemTagDTOList.stream()
                    .map(tagDTO -> new ItemTag(tagDTO.getItemTagName(), item))
                    .collect(Collectors.toList());
            itemTagRepository.saveAll(itemTags);
        }

        // 변경된 상품 정보 저장
        itemRepository.save(item);
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

    @Transactional
    public void deleteItem(Long itemId) {
        try {
            // 상품 ID를 사용하여 해당 상품을 찾습니다.
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId));

            // 상품에 속한 이미지들을 먼저 삭제합니다.
            itemImgRepository.deleteByItemId(itemId);

            // 상품에 속한 태그들을 삭제합니다.
            itemTagRepository.deleteByItemId(itemId);

            // 상품을 삭제합니다.
            itemRepository.delete(item);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("해당 상품이 존재하지 않습니다. id = " + itemId);
        } catch (Exception e) {
            // 다른 예외가 발생한 경우에 대한 처리
            throw new RuntimeException("상품 삭제 중에 오류가 발생했습니다.", e);
        }
    }


    // 조회수 내림차순으로 상위 8개의 상품 조회
    public List<Item> getTop8products() {
        return itemRepository.findTop8ByOrderByViewCountDesc(PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount")));
    }

    public List<ListingItemDTO> getTop8productswithImg() {
        List<Item> topItems = getTop8products();

        // Item 객체를 ListingItemDTO로 변환
        List<ListingItemDTO> listingItemDTOs = topItems.stream().map(item -> {
            // 각 Item에 대한 대표 이미지 조회
            ItemImg repImg = itemImgRepository.findFirstByItemIdAndRepimgYn(item.getId(), "Y").orElse(null);

            // ListingItemDTO 객체 생성 및 필드 설정
            ListingItemDTO listingItemDTO = new ListingItemDTO();
            listingItemDTO.setId(item.getId());
            listingItemDTO.setItemName(item.getItemName());
            listingItemDTO.setPrice(item.getPrice());
            listingItemDTO.setRegdate(item.getRegdate());
            listingItemDTO.setCategoryId(item.getCategory().getId());
            listingItemDTO.setItemStatus(item.getItemStatus());
            listingItemDTO.setSellStatus(item.getSellStatus());
            // 대표 이미지가 존재하는 경우, 이미지 이름 설정
            if (repImg != null) {
                listingItemDTO.setImgName(repImg.getImgName());
            }

            return listingItemDTO;

        }).collect(Collectors.toList());

        return listingItemDTOs;
    }


}
