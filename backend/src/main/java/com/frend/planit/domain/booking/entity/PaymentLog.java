package com.frend.planit.domain.booking.entity;

import com.frend.planit.global.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 응답 전체를 저장하는 PaymentLog 엔티티입니다.
 *
 * <p>아임포트 결제 응답을 기반으로, 결제에 대한 상세 정보를 모두 기록합니다.</p>
 * 실제 예약(Booking)과 1:1로 연결되며, 원본 JSON 응답을 함께 보관합니다.
 *
 * @author zelly
 * @since 2025-04-10
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_log")
@AttributeOverride(name = "id", column = @Column(name = "payment_log_id"))
public class PaymentLog extends BaseEntity {

    /**
     * 아임포트 결제 고유 ID (imp_uid)
     */
    private String impUid;
    /**
     * 주문 고유 ID (merchant_uid)
     */
    private String merchantUid;
    /**
     * 결제 수단 (예: kakaopay, card 등)
     */
    private String payMethod;
    /**
     * PG사 이름 (예: kakaopay, tosspay 등)
     */
    private String pgProvider;
    /**
     * PG사 거래 번호
     */
    private String pgTid;
    /**
     * 결제 금액 (원 단위)
     */
    private Integer paidAmount;
    /**
     * 통화 단위 (기본: KRW)
     */
    private String currency;
    /**
     * 결제 상태 (예: paid, failed)
     */
    private String status;
    /**
     * 결제 완료 시각 (서버 기준)
     */
    private LocalDateTime paidAt;
    /**
     * 영수증 URL
     */
    private String receiptUrl;
    /**
     * 전체 결제 응답 JSON (백업용 원본)
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String responseJson;
    /**
     * 레코드 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 레코드 생성 시간 설정
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
