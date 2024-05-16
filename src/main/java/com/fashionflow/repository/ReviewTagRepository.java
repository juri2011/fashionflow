package com.fashionflow.repository;

import com.fashionflow.entity.Review;
import com.fashionflow.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    List<ReviewTag> findByReview(Review review);
}
