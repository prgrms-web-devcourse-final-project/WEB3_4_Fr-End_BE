package com.frend.planit.domain.booking.repository;

import com.frend.planit.domain.booking.entity.Booking;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingQueryRepository {

    List<Booking> findAllByUserId(Long userId);

}
