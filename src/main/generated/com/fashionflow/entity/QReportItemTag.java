package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportItemTag is a Querydsl query type for ReportItemTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportItemTag extends EntityPathBase<ReportItemTag> {

    private static final long serialVersionUID = 2060277149L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportItemTag reportItemTag = new QReportItemTag("reportItemTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReportItem reportItem;

    public final EnumPath<com.fashionflow.constant.ReportTagItem> reportTagItem = createEnum("reportTagItem", com.fashionflow.constant.ReportTagItem.class);

    public QReportItemTag(String variable) {
        this(ReportItemTag.class, forVariable(variable), INITS);
    }

    public QReportItemTag(Path<? extends ReportItemTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportItemTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportItemTag(PathMetadata metadata, PathInits inits) {
        this(ReportItemTag.class, metadata, inits);
    }

    public QReportItemTag(Class<? extends ReportItemTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportItem = inits.isInitialized("reportItem") ? new QReportItem(forProperty("reportItem"), inits.get("reportItem")) : null;
    }

}

