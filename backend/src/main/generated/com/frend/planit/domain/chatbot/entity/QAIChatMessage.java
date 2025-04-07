package com.frend.planit.domain.chatbot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAIChatMessage is a Querydsl query type for AIChatMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAIChatMessage extends EntityPathBase<AIChatMessage> {

    private static final long serialVersionUID = -865353673L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAIChatMessage aIChatMessage = new QAIChatMessage("aIChatMessage");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath botMessage = createString("botMessage");

    public final QAIChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath userMessage = createString("userMessage");

    public QAIChatMessage(String variable) {
        this(AIChatMessage.class, forVariable(variable), INITS);
    }

    public QAIChatMessage(Path<? extends AIChatMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAIChatMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAIChatMessage(PathMetadata metadata, PathInits inits) {
        this(AIChatMessage.class, metadata, inits);
    }

    public QAIChatMessage(Class<? extends AIChatMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QAIChatRoom(forProperty("chatRoom")) : null;
    }

}

