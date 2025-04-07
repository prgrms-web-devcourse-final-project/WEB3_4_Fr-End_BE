package com.frend.planit.domain.accommodation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccommodationEntity is a Querydsl query type for AccommodationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationEntity extends EntityPathBase<AccommodationEntity> {

    private static final long serialVersionUID = -1741732126L;

    public static final QAccommodationEntity accommodationEntity = new QAccommodationEntity("accommodationEntity");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final ListPath<String, StringPath> amenities = this.<String, StringPath>createList("amenities", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> areaCode = createNumber("areaCode", Integer.class);

    public final NumberPath<Integer> availableRooms = createNumber("availableRooms", Integer.class);

    public final StringPath cat3 = createString("cat3");

    public final StringPath checkInTime = createString("checkInTime");

    public final StringPath checkOutTime = createString("checkOutTime");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath location = createString("location");

    public final StringPath mainImage = createString("mainImage");

    public final NumberPath<Double> mapX = createNumber("mapX", Double.class);

    public final NumberPath<Double> mapY = createNumber("mapY", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final NumberPath<java.math.BigDecimal> pricePerNight = createNumber("pricePerNight", java.math.BigDecimal.class);

    public QAccommodationEntity(String variable) {
        super(AccommodationEntity.class, forVariable(variable));
    }

    public QAccommodationEntity(Path<? extends AccommodationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodationEntity(PathMetadata metadata) {
        super(AccommodationEntity.class, metadata);
    }

}

