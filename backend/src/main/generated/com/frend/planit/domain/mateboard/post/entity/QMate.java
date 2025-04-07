package com.frend.planit.domain.mateboard.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMate is a Querydsl query type for Mate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMate extends EntityPathBase<Mate> {

    private static final long serialVersionUID = -1444362773L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMate mate = new QMate("mate");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<MateGender> mateGender = createEnum("mateGender", MateGender.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> recruitCount = createNumber("recruitCount", Integer.class);

    public final EnumPath<RecruitmentStatus> recruitmentStatus = createEnum("recruitmentStatus", RecruitmentStatus.class);

    public final StringPath title = createString("title");

    public final DatePath<java.time.LocalDate> travelEndDate = createDate("travelEndDate", java.time.LocalDate.class);

    public final EnumPath<TravelRegion> travelRegion = createEnum("travelRegion", TravelRegion.class);

    public final DatePath<java.time.LocalDate> travelStartDate = createDate("travelStartDate", java.time.LocalDate.class);

    public final com.frend.planit.domain.user.entity.QUser writer;

    public QMate(String variable) {
        this(Mate.class, forVariable(variable), INITS);
    }

    public QMate(Path<? extends Mate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMate(PathMetadata metadata, PathInits inits) {
        this(Mate.class, metadata, inits);
    }

    public QMate(Class<? extends Mate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.frend.planit.domain.user.entity.QUser(forProperty("writer")) : null;
    }

}

