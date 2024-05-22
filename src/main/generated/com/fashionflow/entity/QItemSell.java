package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemSell is a Querydsl query type for ItemSell
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemSell extends EntityPathBase<ItemSell> {

    private static final long serialVersionUID = -1626444581L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemSell itemSell = new QItemSell("itemSell");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> sellDate = createDateTime("sellDate", java.time.LocalDateTime.class);

    public QItemSell(String variable) {
        this(ItemSell.class, forVariable(variable), INITS);
    }

    public QItemSell(Path<? extends ItemSell> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemSell(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemSell(PathMetadata metadata, PathInits inits) {
        this(ItemSell.class, metadata, inits);
    }

    public QItemSell(Class<? extends ItemSell> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

