package com.frend.planit.scheduler;

import com.frend.planit.domain.booking.repository.BookingRepository;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusUpdateScheduler {

    private final BookingRepository bookingRepository;
    private final MateRepository mateRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void runBookingUpdate() {
        LocalDate today = LocalDate.now();
        int updated = bookingRepository.updateBookingStatusToCompleted(today);
        log.info("[Booking] 상태 업데이트 완료 - {}건 COMPLETED 처리됨", updated);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void runMateUpdate() {
        LocalDate today = LocalDate.now();
        int updated = mateRepository.updateRecruitmentStatusToClosed(today);
        log.info("[Mate] 모집 상태 업데이트 완료 - {}건 CLOSED 처리됨", updated);
    }
}
