package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemBuyRepository extends JpaRepository<ItemBuy, Long> {
    List<ItemBuy> findByMemberId(Long memberId, Sort sort);

    ItemBuy findItemBuyByItem(Item item);
}
