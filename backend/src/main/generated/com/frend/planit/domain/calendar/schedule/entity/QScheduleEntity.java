package com.frend.planit.domain.calendar.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleEntity is a Querydsl query type for ScheduleEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleEntity extends EntityPathBase<ScheduleEntity> {

    private static final long serialVersionUID = -1978422174L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleEntity scheduleEntity = new QScheduleEntity("scheduleEntity");

    public final TimePath<java.time.LocalTime> alertTime = createTime("alertTime", java.time.LocalTime.class);

    public final com.frend.planit.domain.calendar.entity.QCalendarEntity calendar;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath note = createString("note");

    public final ListPath<com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity, com.frend.planit.domain.calendar.schedule.day.entity.QScheduleDayEntity> scheduleDayList = this.<com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity, com.frend.planit.domain.calendar.schedule.day.entity.QScheduleDayEntity>createList("scheduleDayList", com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity.class, com.frend.planit.domain.calendar.schedule.day.entity.QScheduleDayEntity.class, PathInits.DIRECT2);

    public final StringPath scheduleTitle = createString("scheduleTitle");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QScheduleEntity(String variable) {
        this(ScheduleEntity.class, forVariable(variable), INITS);
    }

    public QScheduleEntity(Path<? extends ScheduleEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleEntity(PathMetadata metadata, PathInits inits) {
        this(ScheduleEntity.class, metadata, inits);
    }

    public QScheduleEntity(Class<? extends ScheduleEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.calendar = inits.isInitialized("calendar") ? new com.frend.planit.domain.calendar.entity.QCalendarEntity(forProperty("calendar")) : null;
    }

}

