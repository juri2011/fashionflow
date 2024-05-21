package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/* QueryDSL을 JPA와 함께 사용하기 위해 작성한 사용자 정의 인터페이스 */
// 커스텀 쿼리 메서드를 정의하는 인터페이스
public interface ReportItemRepositoryCustom {

    // 페이지네이션을 적용하여 아이템 신고를 가져오는 커스텀 메서드
    Page<ReportItem> getReportItemPage(Pageable pageable);
}