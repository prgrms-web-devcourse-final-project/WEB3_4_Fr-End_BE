package com.frend.planit.domain.booking.entity;

import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "booking",
        indexes = {
                @Index(name = "idx_booking_status", columnList = "status")
        },
        uniqueConstraints = {
                // 중복 예약 방지를 위한 제약 조건 (무결성)
                @UniqueConstraint(
                        name = "unique_booking",
                        columnNames = {"accommodation_id", "checkIn", "checkOut", "status"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 예약 PK

    // TODO: User 엔티티와 연관관계 설정 (현재 미구현으로 주석 처리)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    // TODO: 숙소 엔티티와 연관관계 설정 (현재 미구현으로 주석 처리)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "accommodation_id", nullable = false)
    // private Accommodation accommodation;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    // 예약 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    // 예약 인원
    @Column(nullable = false)
    private int person;

    // TODO: 숙소에서 가격을 가져오므로 주석 처리
    // @Column(nullable = false)
    // private int price;

    // 예약 상태 ENUM
    public enum BookingStatus {
        PENDING,   // 예약 대기
        CONFIRMED, // 예약 확정
        CANCELED   // 예약 취소
    }

    // 체크인-체크아웃 유효성 검사
    //BaseTime에는 생성과 수정시간만 포함하고있어 이용 불가능
    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜보다 늦어야 합니다!");
        }
    }
}