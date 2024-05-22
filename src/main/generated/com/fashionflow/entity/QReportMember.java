package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportMember is a Querydsl query type for ReportMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportMember extends EntityPathBase<ReportMember> {

    private static final long serialVersionUID = 305899972L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportMember reportMember = new QReportMember("reportMember");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public final QMember reportedMember;

    public final QMember reporterMember;

    public final EnumPath<com.fashionflow.constant.ReportStatus> reportStatus = createEnum("reportStatus", com.fashionflow.constant.ReportStatus.class);

    public QReportMember(String variable) {
        this(ReportMember.class, forVariable(variable), INITS);
    }

    public QReportMember(Path<? extends ReportMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportMember(PathMetadata metadata, PathInits inits) {
        this(ReportMember.class, metadata, inits);
    }

    public QReportMember(Class<? extends ReportMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedMember = inits.isInitialized("reportedMember") ? new QMember(forProperty("reportedMember"), inits.get("reportedMember")) : null;
        this.reporterMember = inits.isInitialized("reporterMember") ? new QMember(forProperty("reporterMember"), inits.get("reporterMember")) : null;
    }

}

