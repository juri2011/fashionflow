package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 아이템 태그에 대한 데이터 액세스를 처리하는 리포지토리
public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {

    // 아이템 ID에 해당하는 태그 목록을 가져오는 메서드
    public List<ItemTag> findByItemId(Long itemId);

    // 아이템 ID에 해당하는 태그를 삭제하는 메서드
    void deleteByItemId(Long itemId);
}
