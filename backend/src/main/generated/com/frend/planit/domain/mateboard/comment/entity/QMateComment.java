package com.frend.planit.domain.mateboard.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMateComment is a Querydsl query type for MateComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMateComment extends EntityPathBase<MateComment> {

    private static final long serialVersionUID = -128448889L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMateComment mateComment = new QMateComment("mateComment");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.frend.planit.domain.mateboard.post.entity.QMate mate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QMateComment(String variable) {
        this(MateComment.class, forVariable(variable), INITS);
    }

    public QMateComment(Path<? extends MateComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMateComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMateComment(PathMetadata metadata, PathInits inits) {
        this(MateComment.class, metadata, inits);
    }

    public QMateComment(Class<? extends MateComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mate = inits.isInitialized("mate") ? new com.frend.planit.domain.mateboard.post.entity.QMate(forProperty("mate"), inits.get("mate")) : null;
    }

}

