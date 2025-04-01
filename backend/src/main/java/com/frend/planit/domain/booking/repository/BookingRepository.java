package com.frend.planit.domain.booking.repository;

import com.frend.planit.domain.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}