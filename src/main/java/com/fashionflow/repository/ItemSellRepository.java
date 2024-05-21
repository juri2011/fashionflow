package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;

// 아이템 판매에 대한 데이터 액세스를 처리하는 리포지토리
public interface ItemSellRepository extends JpaRepository<ItemSell, Long> {

    // 특정 회원 ID에 해당하는 아이템 판매 수를 세는 메서드
    Long countByMemberId(Long memberId);
}
