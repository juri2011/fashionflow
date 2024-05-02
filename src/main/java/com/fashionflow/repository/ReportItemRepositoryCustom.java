package com.fashionflow.repository;

import com.fashionflow.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/* QueryDSL을 JPA와 함께 사용하기 위해 작성한 사용자 정의 인터페이스 */
public interface ReportItemRepositoryCustom {

    //타입 수정 들어갈 수 있음... DTO로 바꿀 수 있을까?
    //Page<ReportItem>으로 전달하면 Controller에서 ReportItemDTO를 어떻게 사용할지 고민됨
    Page<ReportItem> getReportItemPage(Pageable pageable);
}
