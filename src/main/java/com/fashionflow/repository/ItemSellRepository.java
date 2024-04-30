package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSellRepository extends JpaRepository<ItemSell, Long> {

    Long countByMemberId(Long memberId);
}
