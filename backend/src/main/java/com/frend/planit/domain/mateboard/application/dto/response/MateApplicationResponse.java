package com.frend.planit.domain.mateboard.application.dto.response;

public record MateApplicationResponse(
        Long userId,
        String status // PENDING, ACCEPTED, REJECTED
) {

}