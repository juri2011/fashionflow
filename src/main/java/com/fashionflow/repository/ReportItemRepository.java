package com.fashionflow.repository;

import com.fashionflow.entity.ItemBuy;
import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 아이템 신고에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReportItemRepository extends JpaRepository<ReportItem, Long>,
        ReportItemRepositoryCustom {

    // 등록 날짜 내림차순으로 모든 아이템 신고를 가져오는 메서드
    public List<ReportItem> findAllByOrderByRegdateDesc();

    // ID 내림차순으로 모든 아이템 신고를 페이지로 가져오는 메서드
    public Page<ReportItem> findAllByOrderByIdDesc(Pageable pageable);
}