package com.frend.planit.domain.booking;

import com.frend.planit.domain.booking.dto.request.BookingRequestDto;
import com.frend.planit.domain.booking.dto.response.BookingResponseDto;
import com.frend.planit.domain.booking.entity.BookingEntity;
import com.frend.planit.domain.booking.repository.BookingRepository;
import com.frend.planit.domain.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    private BookingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new BookingRequestDto();
        requestDto.setCheckIn(LocalDate.now());
        requestDto.setCheckOut(LocalDate.now().plusDays(2));
        requestDto.setStatus(BookingEntity.BookingStatus.CONFIRMED);
        requestDto.setPerson(2);
    }

    //예약 생성 테스트 ( 예약 시작 )
    @Test
    void testCreateBooking() {
        BookingResponseDto response = bookingService.createBooking(requestDto);
        assertThat(response).isNotNull();
        assertThat(response.getCheckIn()).isEqualTo(requestDto.getCheckIn());
    }

    //에약 조회 테스트
    @Test
    void testGetBooking() {
        BookingResponseDto saved = bookingService.createBooking(requestDto);
        BookingResponseDto found = bookingService.getBooking(saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
    }

    // 예약 수정 테스트
    @Test
    void testUpdateBooking() {
        BookingResponseDto saved = bookingService.createBooking(requestDto);
        BookingRequestDto updateDto = new BookingRequestDto();
        updateDto.setCheckIn(LocalDate.now().plusDays(1));
        updateDto.setCheckOut(LocalDate.now().plusDays(3));
        updateDto.setStatus(BookingEntity.BookingStatus.PENDING);
        updateDto.setPerson(3);

        BookingResponseDto updated = bookingService.updateBooking(saved.getId(), updateDto);
        assertThat(updated.getPerson()).isEqualTo(3);
        assertThat(updated.getStatus()).isEqualTo(BookingEntity.BookingStatus.PENDING);
    }

    //예약 취소 테스트
    @Test
    void testCancelBooking() {
        BookingResponseDto saved = bookingService.createBooking(requestDto);
        bookingService.cancelBooking(saved.getId());
        BookingResponseDto canceled = bookingService.getBooking(saved.getId());
        assertThat(canceled.getStatus()).isEqualTo(BookingEntity.BookingStatus.CANCELED);
    }

    //예약 삭제 테스트
    @Test
    void testDeleteBooking() {
        BookingResponseDto saved = bookingService.createBooking(requestDto);
        bookingService.cancelBooking(saved.getId());
        Optional<BookingEntity> deleted = bookingRepository.findById(saved.getId());
        assertThat(deleted).isPresent();
        assertThat(deleted.get().getStatus()).isEqualTo(BookingEntity.BookingStatus.CANCELED);
    }
}
