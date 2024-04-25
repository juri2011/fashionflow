package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportItem is a Querydsl query type for ReportItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportItem extends EntityPathBase<ReportItem> {

    private static final long serialVersionUID = 299654333L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportItem reportItem = new QReportItem("reportItem");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public final QItem reportedItem;

    public final QMember reporterMember;

    public final EnumPath<com.fashionflow.constant.ReportStatus> reportStatus = createEnum("reportStatus", com.fashionflow.constant.ReportStatus.class);

    public QReportItem(String variable) {
        this(ReportItem.class, forVariable(variable), INITS);
    }

    public QReportItem(Path<? extends ReportItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportItem(PathMetadata metadata, PathInits inits) {
        this(ReportItem.class, metadata, inits);
    }

    public QReportItem(Class<? extends ReportItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedItem = inits.isInitialized("reportedItem") ? new QItem(forProperty("reportedItem"), inits.get("reportedItem")) : null;
        this.reporterMember = inits.isInitialized("reporterMember") ? new QMember(forProperty("reporterMember")) : null;
    }

}

