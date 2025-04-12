package com.frend.planit.domain.booking.controller;

import com.frend.planit.domain.booking.dto.request.PaymentCompleteRequest;
import com.frend.planit.domain.booking.dto.response.BookingListResponseDto;
import com.frend.planit.domain.booking.service.BookingQueryService;
import com.frend.planit.domain.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예약 관련 요청을 처리하는 컨트롤러 클래스입니다.
 * <p>예약 생성 및 관련된 작업을 처리합니다.</p>
 *
 * @author zelly
 * @since 2025-04-10
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingQueryService bookingQueryService;

    /**
     * 새로운 예약을 생성합니다.
     *
     * @param paymentCompleteRequest 결제 완료 요청 데이터
     * @return HTTP 201 Created 상태 코드
     */
    @PostMapping("/complete")
    public ResponseEntity<Void> completeBooking(
            @RequestBody PaymentCompleteRequest paymentCompleteRequest) {
        bookingService.saveBookingWithPayment(paymentCompleteRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mypage")
    public BookingListResponseDto getMyBookings(@AuthenticationPrincipal Long userId) {
        return bookingQueryService.getMyBookings(userId);
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId,
            @AuthenticationPrincipal Long userId) {
        bookingService.cancelBooking(bookingId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long bookingId,
            @AuthenticationPrincipal Long userId) {
        bookingService.deleteBooking(bookingId, userId);
    }
}
