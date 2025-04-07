package com.frend.planit.domain.calendar.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCalendarEntity is a Querydsl query type for CalendarEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalendarEntity extends EntityPathBase<CalendarEntity> {

    private static final long serialVersionUID = -1413549512L;

    public static final QCalendarEntity calendarEntity = new QCalendarEntity("calendarEntity");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final DateTimePath<java.time.LocalDateTime> alertTime = createDateTime("alertTime", java.time.LocalDateTime.class);

    public final StringPath calendarTitle = createString("calendarTitle");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath note = createString("note");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public QCalendarEntity(String variable) {
        super(CalendarEntity.class, forVariable(variable));
    }

    public QCalendarEntity(Path<? extends CalendarEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCalendarEntity(PathMetadata metadata) {
        super(CalendarEntity.class, metadata);
    }

}

