package com.frend.planit.domain.calendar.schedule.travel.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTravelEntity is a Querydsl query type for TravelEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTravelEntity extends EntityPathBase<TravelEntity> {

    private static final long serialVersionUID = 315865015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTravelEntity travelEntity = new QTravelEntity("travelEntity");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaomapId = createString("kakaomapId");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.frend.planit.domain.calendar.schedule.day.entity.QScheduleDayEntity scheduleDay;

    public final NumberPath<Integer> visitHour = createNumber("visitHour", Integer.class);

    public final NumberPath<Integer> visitMinute = createNumber("visitMinute", Integer.class);

    public QTravelEntity(String variable) {
        this(TravelEntity.class, forVariable(variable), INITS);
    }

    public QTravelEntity(Path<? extends TravelEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTravelEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTravelEntity(PathMetadata metadata, PathInits inits) {
        this(TravelEntity.class, metadata, inits);
    }

    public QTravelEntity(Class<? extends TravelEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.scheduleDay = inits.isInitialized("scheduleDay") ? new com.frend.planit.domain.calendar.schedule.day.entity.QScheduleDayEntity(forProperty("scheduleDay"), inits.get("scheduleDay")) : null;
    }

}

