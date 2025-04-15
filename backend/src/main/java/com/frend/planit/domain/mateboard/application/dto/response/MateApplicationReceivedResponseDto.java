package com.frend.planit.domain.mateboard.application.dto.response;


public record MateApplicationReceivedResponseDto(
        Long applicationId,
        Long matePostId,
        String mateTitle,
        String mateContentPreview,
        Long applicantId,
        String applicantNickname,
        String applicantProfileImage,
        String status // PENDING, ACCEPTED, REJECTED
) {

}
