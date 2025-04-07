package com.frend.planit.domain.mateboard.post.repository;

import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.entity.QMate;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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

        // 1. 데이터 조회 쿼리
        var results = queryFactory
                .select(Projections.constructor(MateResponseDto.class,
                        mate.id,
                        mate.title,
                        mate.content,
                        mate.travelRegion,
                        mate.travelStartDate,
                        mate.travelEndDate,
                        mate.recruitmentStatus,
                        mate.mateGender,
                        mate.writer.nickname,
                        mate.writer.profileImageUrl,
                        mate.createdAt
                ))
                .from(mate)
                .leftJoin(mate.writer, user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(mate.count())
                .from(mate)
                .where(builder);

        // 3. Page 객체로 반환
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
}