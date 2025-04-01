package com.frend.planit.domain.booking.dto.response;

import com.frend.planit.domain.booking.entity.BookingEntity;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class BookingResponseDto {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingEntity.BookingStatus status;
    private int person;

    public BookingResponseDto(BookingEntity booking) {
        this.id = booking.getId();
        this.checkIn = booking.getCheckIn();
        this.checkOut = booking.getCheckOut();
        this.status = booking.getStatus();
        this.person = booking.getPerson();
    }
}
