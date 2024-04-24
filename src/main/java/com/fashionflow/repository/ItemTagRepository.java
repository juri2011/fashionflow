package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {

}
