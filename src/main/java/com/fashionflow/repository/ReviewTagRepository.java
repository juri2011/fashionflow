package com.fashionflow.repository;

import com.fashionflow.entity.Review;
import com.fashionflow.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 리뷰 태그에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    // 특정 리뷰에 해당하는 모든 리뷰 태그를 가져오는 메서드
    List<ReviewTag> findByReview(Review review);
}