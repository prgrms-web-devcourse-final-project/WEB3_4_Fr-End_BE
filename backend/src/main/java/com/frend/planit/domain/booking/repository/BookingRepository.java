package com.frend.planit.domain.booking.repository;

import com.frend.planit.domain.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 예약(Booking) 엔티티에 대한 JPA 리포지토리입니다.
 * <p> 기본 CRUD 메서드를 제공합니다.</p>
 *
 * @author zelly
 * @since 2025-04-10
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
