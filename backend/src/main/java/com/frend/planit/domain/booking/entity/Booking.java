package com.frend.planit.domain.booking.entity;

import com.frend.planit.global.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 정보를 저장하는 Booking 엔티티입니다.
 * <p>숙소 예약 시 사용자, 숙소 정보, 결제 정보 등을 저장합니다.</p>
 * 결제 완료 시점에 생성되며, 예약 상태(예약 완료/취소 등)를 함께 관리합니다.
 *
 * @author zelly
 * @since 2025-04-10
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "booking")
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
public class Booking extends BaseEntity {

    /**
     * 예약자 ID (User 식별자)
     */
    private Long userId;
    /**
     * 숙소 ID (TOUR API 기반 ID)
     */
    private Long accommodationId;
    /**
     * 숙소 이름
     */
    private String accommodationName;
    /**
     * 숙소 주소
     */
    private String accommodationAddress;
    /**
     * 숙소 대표 이미지 (nullable)
     */
    @Column(nullable = true)
    private String accommodationImage;
    /**
     * 체크인 날짜
     */
    private LocalDate checkIn;
    /**
     * 체크아웃 날짜
     */
    private LocalDate checkOut;
    /**
     * 투숙 인원 수
     */
    private Integer guestCount;
    /**
     * 결제 총 금액
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;
    /**
     * 주문 고유 ID (merchant_uid)
     */
    private String merchantUid;
    /**
     * 아임포트 결제 고유 ID (imp_uid)
     */
    private String impUid;
    /**
     * 예약 상태 (예: RESERVED, CANCELLED 등)
     */
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    /**
     * 결제 상태 (예: paid, failed 등)
     */
    private String paymentStatus;
    /**
     * 결제 수단 (예: kakaopay, card 등)
     */
    private String paymentMethod;
    /**
     * 예약 생성 시간 (결제 완료 시점)
     */
    private LocalDateTime reservedAt;

}
