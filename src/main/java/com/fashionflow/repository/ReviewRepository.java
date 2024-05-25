package com.fashionflow.repository;

import com.fashionflow.entity.Item;
import com.fashionflow.entity.Member;
import com.fashionflow.entity.ReportMemberTag;
import com.fashionflow.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 리뷰에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReviewRepository extends JpaRepository<Review, Long>{

    // 회원 이메일과 아이템 ID에 해당하는 리뷰를 찾는 메서드
    Review findByMemberEmailAndItemId(String email, Long itemId);

    // 회원 이메일로 리뷰를 등록 날짜 내림차순으로 가져오는 메서드
    List<Review> findReviewsByMemberEmailOrderByRegDateDesc(String email);

    //판매자 이메일로 리스트 조회
    List<Review> findBySellerEmailOrderByRegDateDesc(String email);

    long countBySellerEmail(String email);
}