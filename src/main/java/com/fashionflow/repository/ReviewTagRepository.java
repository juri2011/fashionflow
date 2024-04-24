package com.fashionflow.repository;

import com.fashionflow.entity.Review;
import com.fashionflow.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

}
