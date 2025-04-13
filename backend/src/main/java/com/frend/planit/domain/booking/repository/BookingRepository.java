package com.frend.planit.domain.booking.repository;

import com.frend.planit.domain.booking.entity.Booking;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예약(Booking) 엔티티에 대한 JPA 리포지토리입니다.
 * <p> 기본 CRUD 메서드를 제공합니다.</p>
 *
 * @author zelly
 * @since 2025-04-10
 */

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.bookingStatus = com.frend.planit.domain.booking.entity.BookingStatus.COMPLETED "
            +
            "WHERE b.checkOutDate < :today AND b.bookingStatus != com.frend.planit.domain.booking.entity.BookingStatus.COMPLETED")
    int updateBookingStatusToCompleted(@Param("today") LocalDate today);
}
