package com.fashionflow.repository;

import com.fashionflow.entity.Heart;
import com.fashionflow.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    public Long countByItemId(Long itemId);

    public List<Heart> findByMember_Email(String email);
}
