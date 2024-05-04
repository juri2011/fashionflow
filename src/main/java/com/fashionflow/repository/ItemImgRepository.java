package com.fashionflow.repository;

import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, QuerydslPredicateExecutor<ItemImg> {

    public List<ItemImg> findByItemId(Long itemId);
    public List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    Optional<ItemImg> findByItemIdAndRepimgYn(Long itemId, String repimgYn);


}
