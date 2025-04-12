package com.frend.planit.domain.booking.repository;

import static com.frend.planit.domain.booking.entity.QBooking.booking;

import com.frend.planit.domain.booking.entity.Booking;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookingQueryRepositoryImpl implements BookingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Booking> findAllByUserId(Long userId) {
        return queryFactory
                .selectFrom(booking)
                .where(booking.userId.eq(userId))
                .orderBy(booking.checkInDate.asc())
                .fetch();
    }
}
