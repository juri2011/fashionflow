package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {
    public List<ItemTag> findByItemId(Long itemId);

    void deleteByItemId(Long itemId);

}
