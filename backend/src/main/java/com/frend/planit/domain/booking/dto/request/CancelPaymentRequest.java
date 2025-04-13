package com.frend.planit.domain.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelPaymentRequest {

    private String imp_uid; // 필수
    private Integer amount; // 취소할 금액
    private String reason = "사용자 요청"; // 기본값

}
