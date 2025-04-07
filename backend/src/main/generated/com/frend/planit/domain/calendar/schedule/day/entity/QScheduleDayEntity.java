package com.frend.planit.domain.calendar.schedule.day.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleDayEntity is a Querydsl query type for ScheduleDayEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleDayEntity extends EntityPathBase<ScheduleDayEntity> {

    private static final long serialVersionUID = -56356270L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleDayEntity scheduleDayEntity = new QScheduleDayEntity("scheduleDayEntity");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Integer> dayOrder = createNumber("dayOrder", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.frend.planit.domain.calendar.schedule.entity.QScheduleEntity schedule;

    public final ListPath<com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity, com.frend.planit.domain.calendar.schedule.travel.entity.QTravelEntity> travelList = this.<com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity, com.frend.planit.domain.calendar.schedule.travel.entity.QTravelEntity>createList("travelList", com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity.class, com.frend.planit.domain.calendar.schedule.travel.entity.QTravelEntity.class, PathInits.DIRECT2);

    public QScheduleDayEntity(String variable) {
        this(ScheduleDayEntity.class, forVariable(variable), INITS);
    }

    public QScheduleDayEntity(Path<? extends ScheduleDayEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleDayEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleDayEntity(PathMetadata metadata, PathInits inits) {
        this(ScheduleDayEntity.class, metadata, inits);
    }

    public QScheduleDayEntity(Class<? extends ScheduleDayEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new com.frend.planit.domain.calendar.schedule.entity.QScheduleEntity(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

