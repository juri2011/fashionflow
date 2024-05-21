package com.fashionflow.repository;

import com.fashionflow.entity.ItemImg;
import com.fashionflow.entity.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

// 아이템 이미지에 대한 데이터 액세스를 처리하는 리포지토리
public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, QuerydslPredicateExecutor<ItemImg> {

    // 아이템 ID에 해당하는 이미지 목록을 가져오는 메서드
    public List<ItemImg> findByItemId(Long itemId);

    // 아이템 ID에 해당하는 이미지 목록을 ID의 오름차순으로 가져오는 메서드
    public List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    // 아이템 ID와 대표 이미지 여부에 해당하는 이미지를 찾는 메서드
    Optional<ItemImg> findByItemIdAndRepimgYn(Long itemId, String repimgYn);

    // 아이템 ID와 대표 이미지 여부에 해당하는 첫 번째 이미지를 찾는 메서드
    Optional<ItemImg> findFirstByItemIdAndRepimgYn(Long itemId, String y);

    // 아이템 ID에 해당하는 이미지를 삭제하는 메서드
    void deleteByItemId(Long itemId);
}

