package com.frend.planit.domain.booking.dto.request;

import com.frend.planit.domain.booking.entity.BookingEntity;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class BookingRequestDto {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingEntity.BookingStatus status;
    private int person;

    public BookingEntity toEntity() {
        return BookingEntity.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .status(status)
                .person(person)
                .build();
    }
}