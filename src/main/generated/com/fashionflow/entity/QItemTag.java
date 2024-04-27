package com.fashionflow.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemTag is a Querydsl query type for ItemTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemTag extends EntityPathBase<ItemTag> {

    private static final long serialVersionUID = -1437938447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemTag itemTag = new QItemTag("itemTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final EnumPath<com.fashionflow.constant.ItemTagName> itemTagName = createEnum("itemTagName", com.fashionflow.constant.ItemTagName.class);

    public QItemTag(String variable) {
        this(ItemTag.class, forVariable(variable), INITS);
    }

    public QItemTag(Path<? extends ItemTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemTag(PathMetadata metadata, PathInits inits) {
        this(ItemTag.class, metadata, inits);
    }

    public QItemTag(Class<? extends ItemTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

