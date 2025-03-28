package com.frend.planit.domain.booking.entity;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.accommodation.entity.Accommodation;
import com.frend.planit.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "booking",
        indexes = {
                @Index(name = "idx_booking_user", columnList = "user_id"),
                @Index(name = "idx_booking_accodation", columnLommist = "accommodation_id"),
                @Index(name = "idx_booking_checkin_checkout", columnList = "check_in, check_out")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;  // 숙소 이름 저장

    @Column(nullable = false)
    private LocalDate checkIn;  // 체크인 날짜

    @Column(nullable = false)
    private LocalDate checkOut;  // 체크아웃 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;  // 예약 상태 아래내용참고

    @Column(nullable = false)
    private int person; // 예약 인원수

    @Column(nullable = false)
    private int price;

    // 예약 상태
    public enum BookingStatus {
        PENDING,   // 예약 대기
        CONFIRMED, // 예약 확정
        CANCELED   // 예약 취소
    }
}