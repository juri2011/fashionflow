package com.fashionflow.repository;

import com.fashionflow.entity.QReportItem;
import com.fashionflow.entity.ReportItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

// ReportItemRepositoryCustom 인터페이스를 구현하는 커스텀 리포지토리 구현체
public class ReportItemRepositoryCustomImpl implements ReportItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 생성자 주입을 통한 EntityManager 설정
    public ReportItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ReportItem> getReportItemPage(Pageable pageable) {
        // 쿼리팩토리를 사용하여 쿼리 실행
        List<ReportItem> results = queryFactory
                .selectFrom(QReportItem.reportItem)
                //.where()  // 필요한 경우 where 조건 추가
                .orderBy(QReportItem.reportItem.id.desc())  // ID 내림차순 정렬
                .offset(pageable.getOffset())  // 페이지네이션 오프셋 설정
                .limit(pageable.getPageSize())  // 페이지 크기 설정
                .fetch();  // 결과 가져오기

        long total = results.size();  // 전체 결과 수

        // Page 인터페이스에 맞게 결과 반환
        return new PageImpl<>(results, pageable, total);
    }
}
