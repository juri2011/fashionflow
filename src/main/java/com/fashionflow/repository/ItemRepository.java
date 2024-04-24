package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
