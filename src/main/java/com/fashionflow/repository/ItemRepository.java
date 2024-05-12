package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    public List<Item> findByMemberId(Long memberId);

    public Item findItemById(Long id);
}
