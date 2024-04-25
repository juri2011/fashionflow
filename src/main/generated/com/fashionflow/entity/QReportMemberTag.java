package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportMemberTag is a Querydsl query type for ReportMemberTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportMemberTag extends EntityPathBase<ReportMemberTag> {

    private static final long serialVersionUID = -854452426L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportMemberTag reportMemberTag = new QReportMemberTag("reportMemberTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReportMember reportMember;

    public final EnumPath<com.fashionflow.constant.ReportTagMember> reportTagMember = createEnum("reportTagMember", com.fashionflow.constant.ReportTagMember.class);

    public QReportMemberTag(String variable) {
        this(ReportMemberTag.class, forVariable(variable), INITS);
    }

    public QReportMemberTag(Path<? extends ReportMemberTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportMemberTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportMemberTag(PathMetadata metadata, PathInits inits) {
        this(ReportMemberTag.class, metadata, inits);
    }

    public QReportMemberTag(Class<? extends ReportMemberTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportMember = inits.isInitialized("reportMember") ? new QReportMember(forProperty("reportMember"), inits.get("reportMember")) : null;
    }

}

