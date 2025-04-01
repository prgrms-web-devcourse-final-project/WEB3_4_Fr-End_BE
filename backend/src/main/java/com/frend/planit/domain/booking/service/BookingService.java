package com.frend.planit.domain.booking.service;

import com.frend.planit.booking.dto.request.BookingRequestDto;
import com.frend.planit.booking.dto.response.BookingResponseDto;
import com.frend.planit.domain.booking.entity.BookingEntity;
import com.frend.planit.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto requestDto) {
        BookingEntity booking = requestDto.toEntity();
        return new BookingResponseDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto updateBooking(Long id, BookingRequestDto requestDto) {
        BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        booking.setCheckIn(requestDto.getCheckIn());
        booking.setCheckOut(requestDto.getCheckOut());
        booking.setStatus(requestDto.getStatus());
        booking.setPerson(requestDto.getPerson());
        return new BookingResponseDto(booking);
    }

    @Transactional
    public void cancelBooking(Long id) {
        BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        booking.setStatus(BookingEntity.BookingStatus.CANCELED);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBooking(Long id) {
        BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        return new BookingResponseDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingResponseDto::new).collect(Collectors.toList());
    }
}
