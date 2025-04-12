package com.frend.planit.domain.booking.service;

import com.frend.planit.domain.booking.dto.response.BookingListResponseDto;
import com.frend.planit.domain.booking.dto.response.BookingSummaryDto;
import com.frend.planit.domain.booking.entity.Booking;
import com.frend.planit.domain.booking.entity.BookingStatus;
import com.frend.planit.domain.booking.repository.BookingQueryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingQueryService {

    private final BookingQueryRepository bookingQueryRepository;

    public BookingListResponseDto getMyBookings(Long userId) {

        // 유저가 한 모든 예약 내역 불러옴
        List<Booking> bookings = bookingQueryRepository.findAllByUserId(userId);

        // checkInDate와 비교할 기준 날짜(오늘)
        LocalDate today = LocalDate.now();

        List<BookingSummaryDto> upcoming = new ArrayList<>();
        List<BookingSummaryDto> pastOrCanceled = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingSummaryDto dto = BookingSummaryDto.builder()
                    .bookingId(booking.getId())
                    .accommodationName(booking.getAccommodationName())
                    .accommodationAddress(booking.getAccommodationAddress())
                    .accommodationImage(booking.getAccommodationImage())
                    .checkInDate(booking.getCheckInDate())
                    .checkInTime(booking.getCheckInTime())
                    .paymentStatus(booking.getPaymentStatus())
                    .reservedAt(booking.getReservedAt())
                    .canCancel(canCancel(booking, today))
                    .build();

            if (booking.getBookingStatus().equals(BookingStatus.RESERVED)
                    && !booking.getCheckInDate()
                    .isBefore(today)) {
                upcoming.add(dto);
            } else {
                pastOrCanceled.add(dto);
            }
        }
        return new BookingListResponseDto(upcoming, pastOrCanceled);
    }

    private boolean canCancel(Booking booking, LocalDate today) {
        return booking.getBookingStatus().equals(BookingStatus.RESERVED)
                && booking.getPaymentStatus().equals("PAID")
                && booking.getCheckInDate().isAfter(today);
    }
}