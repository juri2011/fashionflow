package com.fashionflow.repository;

import com.fashionflow.entity.Heart;
import com.fashionflow.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    public Long countByItemId(Long itemId);
}
