package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ItemBuy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemBuyRepository extends JpaRepository<ItemBuy, Long> {

}
