package com.frend.planit.domain.booking.controller;

import com.frend.planit.domain.booking.dto.request.BookingRequestDto;
import com.frend.planit.domain.booking.dto.response.BookingResponseDto;
import com.frend.planit.domain.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto requestDto) {
        return ResponseEntity.ok(bookingService.createBooking(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id, @RequestBody BookingRequestDto requestDto) {
        return ResponseEntity.ok(bookingService.updateBooking(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}
