package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>{

    Review findByMemberEmailAndItemId(String email, Long itemId);

    // 아이템의 회원 이메일로 리뷰를 찾는 메소드 추가
    List<Review> findReviewsByMemberEmailOrderByRegDateDesc(String email);

    //판매자 이름으로 리스트 조회
    List<Review> findBySellerEmailOrderByRegDateDesc(String email);
}
