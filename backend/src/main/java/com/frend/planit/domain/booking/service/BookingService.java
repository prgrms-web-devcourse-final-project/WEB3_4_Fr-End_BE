package com.frend.planit.domain.booking.service;

import static com.frend.planit.global.response.ErrorType.BOOKING_CANNOT_DELETE;
import static com.frend.planit.global.response.ErrorType.BOOKING_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.INVALID_BOOKING_CANCEL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.booking.dto.request.PaymentCompleteRequest;
import com.frend.planit.domain.booking.entity.Booking;
import com.frend.planit.domain.booking.entity.BookingStatus;
import com.frend.planit.domain.booking.entity.PaymentLog;
import com.frend.planit.domain.booking.repository.BookingRepository;
import com.frend.planit.domain.booking.repository.PaymentLogRepository;
import com.frend.planit.global.exception.ServiceException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 완료 응답을 기반으로 예약 및 결제 로그를 저장하는 서비스입니다.
 *
 * @author zelly
 * @since 2025-04-10
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentLogRepository paymentLogRepository;

    private final IamportClient iamportClient;
    private final IamportTokenService iamportTokenService;

    public void saveBookingWithPayment(PaymentCompleteRequest paymentCompleteRequest) {
        /**
         * 결제 완료 시 결제 로그와 예약 정보를 함께 저장
         */
        PaymentLog paymentLog = buildPaymentLogFromRequest(paymentCompleteRequest);
        paymentLogRepository.save(paymentLog);

        Booking booking = buildBookingFromRequest(paymentCompleteRequest);
        bookingRepository.save(booking);
    }

    /**
     * Unix timestamp → LocalDateTime 변환
     */
    private PaymentLog buildPaymentLogFromRequest(PaymentCompleteRequest paymentCompleteRequest) {
        return PaymentLog.builder()
                .impUid(paymentCompleteRequest.getImpUid())
                .merchantUid(paymentCompleteRequest.getMerchantUid())
                .payMethod(paymentCompleteRequest.getPayMethod())
                .pgProvider(paymentCompleteRequest.getPgProvider())
                .pgTid(paymentCompleteRequest.getPgTid())
                .paidAmount(paymentCompleteRequest.getPaidAmount())
                .currency(paymentCompleteRequest.getCurrency())
                .status(paymentCompleteRequest.getStatus())
                .paidAt(convertUnixTimestamp(paymentCompleteRequest.getPaidAt()))
                .receiptUrl(paymentCompleteRequest.getReceiptUrl())
                .responseJson(convertToJson(paymentCompleteRequest)) // 전체 응답 저장 (선택)
                .build();
    }

    /**
     * 예약 정보 생성
     */
    private Booking buildBookingFromRequest(PaymentCompleteRequest paymentCompleteRequest) {
        var data = paymentCompleteRequest.getCustomData();

        return Booking.builder()
                .userId(data.getUserId())
                .accommodationId(data.getAccommodationId())
                .accommodationName(data.getAccommodationName())
                .accommodationAddress(data.getAccommodationAddress())
                .accommodationImage(data.getAccommodationImage())
                .checkInDate(data.getCheckInDate())
                .checkOutDate(data.getCheckOutDate())
                .checkInTime(data.getCheckInTime())
                .guestCount(data.getGuestCount())
                .totalPrice(data.getTotalPrice())
                .merchantUid(paymentCompleteRequest.getMerchantUid())
                .impUid(paymentCompleteRequest.getImpUid())
                .paymentStatus(paymentCompleteRequest.getStatus())
                .paymentMethod(paymentCompleteRequest.getPayMethod())
                .bookingStatus(BookingStatus.RESERVED)
                .reservedAt(convertUnixTimestamp(paymentCompleteRequest.getPaidAt()))
                .build();
    }

    // 유닉스 타임스탬프 → LocalDateTime 변환
    private LocalDateTime convertUnixTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }

    /**
     * DTO → JSON 문자열 직렬화
     */
    private String convertToJson(PaymentCompleteRequest req) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            log.error("결제 응답 직렬화 실패", e);
            return "{}";
        }
    }

    public void cancelBooking(Long bookingId, Long userId) {
        // 1. 예약 조회
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ServiceException(BOOKING_NOT_FOUND));

        // 2. 이미 취소된 예약인지 확인
        if (booking.getBookingStatus() == BookingStatus.CANCELED) {
            throw new ServiceException(INVALID_BOOKING_CANCEL);
        }

        // 3. 아임포트 토큰 발급 (IamportClient 메서드 추가 필요)
        String token = iamportTokenService.getAccessToken();

        // 4. 아임포트에 결제 취소 요청
        iamportClient.cancelPayment(booking.getImpUid(), token);

        // 5. 예약 상태 변경 후 저장
        booking.cancel(); // => 내부적으로 BookingStatus.CANCELLED 변경
    }

    @Transactional
    public void deleteBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ServiceException(BOOKING_NOT_FOUND));

        // 예약 상태 확인
        if (!(booking.getBookingStatus() == BookingStatus.CANCELED ||
                booking.getBookingStatus() == BookingStatus.COMPLETED)) {
            throw new ServiceException(BOOKING_CANNOT_DELETE);
        }

        bookingRepository.delete(booking);
    }
}
