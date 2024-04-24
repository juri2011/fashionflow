package com.fashionflow.repository;

import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
