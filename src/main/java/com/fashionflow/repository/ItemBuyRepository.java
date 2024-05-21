package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 아이템 구매에 대한 데이터 액세스를 처리하는 리포지토리
public interface ItemBuyRepository extends JpaRepository<ItemBuy, Long> {

    // 아이템에 해당하는 구매 정보를 반환하는 메서드
    ItemBuy findItemBuyByItem(Item item);

    // 특정 회원 ID에 해당하는 구매 정보를 페이지로 반환하는 메서드
    Page<ItemBuy> findByMemberId(Long memberId, Pageable pageable);

}
