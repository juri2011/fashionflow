package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberAuthenticationCode is a Querydsl query type for MemberAuthenticationCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAuthenticationCode extends EntityPathBase<MemberAuthenticationCode> {

    private static final long serialVersionUID = 1207541909L;

    public static final QMemberAuthenticationCode memberAuthenticationCode = new QMemberAuthenticationCode("memberAuthenticationCode");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deleteDate = createDateTime("deleteDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final BooleanPath isVerified = createBoolean("isVerified");

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public QMemberAuthenticationCode(String variable) {
        super(MemberAuthenticationCode.class, forVariable(variable));
    }

    public QMemberAuthenticationCode(Path<? extends MemberAuthenticationCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberAuthenticationCode(PathMetadata metadata) {
        super(MemberAuthenticationCode.class, metadata);
    }

}

