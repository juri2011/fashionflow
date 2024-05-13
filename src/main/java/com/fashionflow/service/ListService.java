package com.fashionflow.service;

import com.fashionflow.constant.ItemStatus;
import com.fashionflow.constant.SellStatus;
import com.fashionflow.dto.BuyerDTO;
import com.fashionflow.dto.ItemFormDTO;
import com.fashionflow.dto.ListingItemDTO;
import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemImg;
import com.fashionflow.repository.ItemImgRepository;
import com.fashionflow.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;


    public Page<ListingItemDTO> listingItemWithImgAndCategories(Pageable pageable, List<Long> categoryList, List<Long> saleStatusList, List<Long> productCategoryList, int minPrice, int maxPrice) {


        Page<Item> listingItems = itemRepository.findAll(withCategory(categoryList, saleStatusList, productCategoryList, minPrice, maxPrice), pageable);

        List<ListingItemDTO> listingItemDTO = new ArrayList<>();

        for (Item listingItem : listingItems) {
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(listingItem.getId(), "Y")
                    .orElse(null); // 대표 이미지가 없는 경우를 대비한 처리

            // 임시 기본 이미지
            String imgName = img != null ? img.getImgName() : "/img/default.PNG";


            // listingItemDTO 생성자에 올바른 값 전달
            ListingItemDTO dto = ListingItemDTO.builder()
                    .id(listingItem.getId())
                    .itemName(listingItem.getItemName())
                    .price(listingItem.getPrice())
                    .regdate(listingItem.getRegdate())
                    .itemStatus(listingItem.getItemStatus())
                    .sellStatus(listingItem.getSellStatus())
                    .categoryId(listingItem.getCategory().getId())
                    .imgName(imgName)
                    .build();

            // 판매 상태가 SELLING(판매중)이거나 SOLD_OUT(판매완료)인 경우에만 리스트에 추가
            if (SellStatus.SELLING.getCode().equals(dto.getSellStatus().getCode()) || SellStatus.SOLD_OUT.getCode().equals(dto.getSellStatus().getCode())) {
                listingItemDTO.add(dto);
            }
        }

        return new PageImpl<>(listingItemDTO, pageable, listingItems.getTotalElements());
    }

    //조건 검색
    public static Specification<Item> withCategory(List<Long> categories, List<Long> saleStatuses, List<Long> productCategories, int minPrice, int maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            // 상품 상태
            if (categories != null && !categories.isEmpty()) {
                // ItemStatus enum의 code 값을 기준으로 검색 조건 추가
                predicates.add(root.get("itemStatus").in(categories.stream()
                        .map(category -> ItemStatus.values()[Integer.parseInt(category.toString()) - 1])
                        .toArray()));
            }
            // 판매 상태
            if (saleStatuses != null && !saleStatuses.isEmpty()) {
                // ItemStatus enum의 code 값을 기준으로 검색 조건 추가
                predicates.add(root.get("sellStatus").in(saleStatuses.stream()
                        .map(status -> SellStatus.values()[Integer.parseInt(status.toString()) - 1])
                        .toArray()));
            }

            //상품 카테고리
            if (productCategories != null && !productCategories.isEmpty()) {
                predicates.add(root.join("category").get("id").in(productCategories));
            }

            // minPrice 조건 추가
            if (minPrice > 0) { // minPrice가 0보다 클 때만 조건을 적용
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            // maxPrice 조건 추가
            if (maxPrice > 0) { // maxPrice가 null이 아니고, 0보다 클 때만 조건을 적용
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            // 올바른 타입으로 배열 변환
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

}
