package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemBuy is a Querydsl query type for ItemBuy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemBuy extends EntityPathBase<ItemBuy> {

    private static final long serialVersionUID = -1437955107L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemBuy itemBuy = new QItemBuy("itemBuy");

    public final DateTimePath<java.time.LocalDateTime> buyDate = createDateTime("buyDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QMember member;

    public final BooleanPath reviewExists = createBoolean("reviewExists");

    public QItemBuy(String variable) {
        this(ItemBuy.class, forVariable(variable), INITS);
    }

    public QItemBuy(Path<? extends ItemBuy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemBuy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemBuy(PathMetadata metadata, PathInits inits) {
        this(ItemBuy.class, metadata, inits);
    }

    public QItemBuy(Class<? extends ItemBuy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

