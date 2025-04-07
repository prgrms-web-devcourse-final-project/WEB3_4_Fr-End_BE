package com.frend.planit.domain.chatbot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAIChatSummaryMessage is a Querydsl query type for AIChatSummaryMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAIChatSummaryMessage extends EntityPathBase<AIChatSummaryMessage> {

    private static final long serialVersionUID = 1220924241L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAIChatSummaryMessage aIChatSummaryMessage = new QAIChatSummaryMessage("aIChatSummaryMessage");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath botMessage = createString("botMessage");

    public final QAIChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> endMessageIndex = createNumber("endMessageIndex", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> startMessageIndex = createNumber("startMessageIndex", Integer.class);

    public final StringPath userMessage = createString("userMessage");

    public QAIChatSummaryMessage(String variable) {
        this(AIChatSummaryMessage.class, forVariable(variable), INITS);
    }

    public QAIChatSummaryMessage(Path<? extends AIChatSummaryMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAIChatSummaryMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAIChatSummaryMessage(PathMetadata metadata, PathInits inits) {
        this(AIChatSummaryMessage.class, metadata, inits);
    }

    public QAIChatSummaryMessage(Class<? extends AIChatSummaryMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QAIChatRoom(forProperty("chatRoom")) : null;
    }

}

