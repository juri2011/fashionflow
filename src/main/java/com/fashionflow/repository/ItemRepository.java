package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public List<Item> findByMemberId(Long memberId);
}
