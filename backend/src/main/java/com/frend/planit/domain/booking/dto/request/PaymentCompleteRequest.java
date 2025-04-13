package com.frend.planit.domain.booking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 아임포트 결제 완료 후 전달받는 요청 DTO입니다.
 * <p>프론트엔드에서 전달하는 전체 결제 응답 정보를 담고 있습니다.</p>
 *
 * @author zelly
 * @since 2025-04-10
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompleteRequest {

    @Schema(description = "아임포트 결제 고유 ID")
    @JsonProperty("imp_uid")
    private String impUid;

    @Schema(description = "주문 고유 ID (merchant_uid)")
    @JsonProperty("merchant_uid")
    private String merchantUid;

    @Schema(description = "결제 수단 (예: kakaopay, card)")
    @JsonProperty("pay_method")
    private String payMethod;

    @JsonProperty("pg_provider")
    @Schema(description = "PG사 이름 (예: kakaopay, tosspay)")
    private String pgProvider;

    @JsonProperty("pg_tid")
    @Schema(description = "PG사 거래 번호")
    private String pgTid;

    @JsonProperty("paid_amount")
    @Schema(description = "결제 금액 (원)")
    private Integer paidAmount;

    @Schema(description = "통화 단위 (기본: KRW)")
    private String currency;

    @Schema(description = "결제 상태 (예: paid, failed)")
    private String status;

    @Schema(description = "결제 완료 시간 (Unix timestamp)")
    @JsonProperty("paid_at")
    private Long paidAt;

    @JsonProperty("receipt_url")
    @Schema(description = "영수증 URL")
    private String receiptUrl;

    @Schema(description = "커스텀 예약 정보")
    @JsonProperty("custom_data")
    private CustomData customData;

    /**
     * 예약 생성에 필요한 커스텀 데이터입니다.
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomData {

        @Schema(description = "사용자 ID")
        @JsonProperty("user_id")
        private Long userId;

        @Schema(description = "숙소 ID (TOUR API 기준)")
        @JsonProperty("accommodation_id")
        private Long accommodationId;

        @Schema(description = "숙소 이름")
        @JsonProperty("accommodation_name")
        private String accommodationName;

        @Schema(description = "숙소 주소")
        @JsonProperty("accommodation_address")
        private String accommodationAddress;

        @Schema(description = "숙소 이미지 URL")
        @JsonProperty("accommodation_image")
        private String accommodationImage;

        @Schema(description = "체크인 날짜")
        @JsonProperty("check_in_date")
        private LocalDate checkInDate;

        @Schema(description = "체크아웃 날짜")
        @JsonProperty("check_out_date")
        private LocalDate checkOutDate;

        @Schema(description = "체크인 시간")
        @JsonProperty("check_in_time")
        private LocalTime checkInTime;

        @Schema(description = "투숙 인원 수")
        @JsonProperty("guest_count")
        private Integer guestCount;

        @Schema(description = "총 결제 금액")
        @JsonProperty("total_price")
        private BigDecimal totalPrice;
    }
}
