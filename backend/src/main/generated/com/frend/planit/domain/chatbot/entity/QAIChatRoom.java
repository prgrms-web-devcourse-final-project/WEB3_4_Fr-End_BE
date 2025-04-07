package com.frend.planit.domain.chatbot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAIChatRoom is a Querydsl query type for AIChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAIChatRoom extends EntityPathBase<AIChatRoom> {

    private static final long serialVersionUID = 1014509227L;

    public static final QAIChatRoom aIChatRoom = new QAIChatRoom("aIChatRoom");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<AIChatMessage, QAIChatMessage> messages = this.<AIChatMessage, QAIChatMessage>createList("messages", AIChatMessage.class, QAIChatMessage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<AIChatSummaryMessage, QAIChatSummaryMessage> summaryMessages = this.<AIChatSummaryMessage, QAIChatSummaryMessage>createList("summaryMessages", AIChatSummaryMessage.class, QAIChatSummaryMessage.class, PathInits.DIRECT2);

    public final StringPath systemMessage = createString("systemMessage");

    public final StringPath systemStrategyMessage = createString("systemStrategyMessage");

    public QAIChatRoom(String variable) {
        super(AIChatRoom.class, forVariable(variable));
    }

    public QAIChatRoom(Path<? extends AIChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAIChatRoom(PathMetadata metadata) {
        super(AIChatRoom.class, metadata);
    }

}

