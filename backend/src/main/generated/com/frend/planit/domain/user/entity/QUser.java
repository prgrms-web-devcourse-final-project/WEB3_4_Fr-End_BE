package com.frend.planit.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -787299057L;

    public static final QUser user = new QUser("user");

    public final com.frend.planit.global.base.QBaseTime _super = new com.frend.planit.global.base.QBaseTime(this);

    public final StringPath bio = createString("bio");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final EnumPath<com.frend.planit.domain.user.enums.Gender> gender = createEnum("gender", com.frend.planit.domain.user.enums.Gender.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final EnumPath<com.frend.planit.domain.user.enums.LoginType> loginType = createEnum("loginType", com.frend.planit.domain.user.enums.LoginType.class);

    public final BooleanPath mailingType = createBoolean("mailingType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final StringPath phone = createString("phone");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<com.frend.planit.domain.user.enums.Role> role = createEnum("role", com.frend.planit.domain.user.enums.Role.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<com.frend.planit.domain.user.enums.SocialType> socialType = createEnum("socialType", com.frend.planit.domain.user.enums.SocialType.class);

    public final EnumPath<com.frend.planit.domain.user.enums.UserStatus> status = createEnum("status", com.frend.planit.domain.user.enums.UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

