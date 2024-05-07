package com.fashionflow.repository;

import com.fashionflow.entity.QReportItem;
import com.fashionflow.entity.ReportItem;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ReportItemRepositoryCustomImpl implements ReportItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    //생성자 주입
    public ReportItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ReportItem> getReportItemPage(Pageable pageable) {
        List<ReportItem> results = queryFactory
                .selectFrom(QReportItem.reportItem)
                //.where()
                .orderBy(QReportItem.reportItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();

        return new PageImpl<>(results, pageable, total);
    }
}
