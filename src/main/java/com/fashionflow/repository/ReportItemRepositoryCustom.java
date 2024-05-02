package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/* QueryDSL을 JPA와 함께 사용하기 위해 작성한 사용자 정의 인터페이스 */
public interface ReportItemRepositoryCustom {

    Page<ReportItem> getReportItemPage(Pageable pageable);
}
