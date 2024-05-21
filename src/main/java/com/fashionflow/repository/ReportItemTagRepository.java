package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import com.fashionflow.entity.ReportItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 신고 아이템 태그에 대한 데이터 액세스를 처리하는 리포지토리
public interface ReportItemTagRepository extends JpaRepository<ReportItemTag, Long> {

    // 주어진 신고 아이템 ID에 해당하는 모든 신고 아이템 태그를 가져오는 메서드
    public List<ReportItemTag> findAllByReportItem_Id(Long reportId);
}