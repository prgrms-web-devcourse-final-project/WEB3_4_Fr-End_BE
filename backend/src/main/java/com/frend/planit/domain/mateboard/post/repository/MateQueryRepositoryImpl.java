package com.frend.planit.domain.mateboard.post.repository;

import com.frend.planit.domain.image.entity.QImage;
import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.domain.mateboard.application.entity.MateApplicationStatus;
import com.frend.planit.domain.mateboard.application.entity.QMateApplication;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.dto.response.QMateResponseDto;
import com.frend.planit.domain.mateboard.post.entity.QMate;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

/**
 * MateQueryRepositoryImpl
 * <p>QueryDSL을 사용하여 메이트 모집 게시글에 대한 동적 검색 및 페이징 처리를 담당하는 커스텀 리포지토리 구현체입니다.</p>
 *
 * <ul>
 *   <li>키워드를 기반으로 제목, 내용, 작성자 닉네임에서 검색</li>
 *   <li>모집 상태(OPEN, CLOSED) 필터링</li>
 *   <li>지역(서울, 부산 등) 필터링</li>
 *   <li>페이징 처리 및 count 쿼리 최적화 적용</li>
 * </ul>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-07
 */
@Repository
@RequiredArgsConstructor
public class MateQueryRepositoryImpl implements MateQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MateResponseDto> searchMatePosts(String keyword, String status, String region,
            Pageable pageable) {

        QMate mate = QMate.mate;
        QUser user = QUser.user;
        QImage image = QImage.image;
        QMateApplication application = QMateApplication.mateApplication;

        BooleanBuilder builder = new BooleanBuilder();

        // 1. 키워드 검색 조건 추가
        if (keyword != null && !keyword.isBlank()) {
            builder.or(mate.title.containsIgnoreCase(keyword));
            builder.or(mate.content.containsIgnoreCase(keyword));
            builder.or(mate.writer.nickname.containsIgnoreCase(keyword));
        }

        // 2. 모집 상태 필터링
        if (status != null && !status.equals("ALL")) {
            RecruitmentStatus recruitmentStatus = RecruitmentStatus.valueOf(status);
            builder.and(mate.recruitmentStatus.eq(recruitmentStatus));
        }

        // 3. 지역 필터링
        if (region != null && !region.equals("ALL")) {
            TravelRegion travelRegion = TravelRegion.valueOf(region);
            builder.and(mate.travelRegion.eq(travelRegion));
        }

        // 데이터 조회 쿼리
        var results = queryFactory
                .select(new QMateResponseDto(
                        mate.id,
                        mate.writer.id,
                        mate.title,
                        mate.content,
                        mate.travelRegion,
                        mate.travelStartDate,
                        mate.travelEndDate,
                        mate.recruitmentStatus,
                        mate.mateGender,
                        mate.recruitCount,
                        Expressions.numberTemplate(
                                Integer.class,
                                "({0})",
                                JPAExpressions
                                        .select(application.count())
                                        .from(application)
                                        .where(application.mate.eq(mate)
                                                .and(application.status.eq(
                                                        MateApplicationStatus.ACCEPTED)))
                        ),
                        JPAExpressions
                                .select(image.url)
                                .from(image)
                                .where(image.holderType.eq(HolderType.MATEBOARD)
                                        .and(image.holderId.eq(mate.id)))
                                .orderBy(image.id.asc())
                                .limit(1),
                        user.nickname,
                        user.bio,
                        user.profileImageUrl,
                        user.gender,
                        mate.createdAt
                ))
                .from(mate)
                .leftJoin(mate.writer, user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(mate.count())
                .from(mate)
                .where(builder);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
}