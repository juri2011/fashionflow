package com.fashionflow.repository;

import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    public List<ItemImg> findByItemId(Long itemId);
}
