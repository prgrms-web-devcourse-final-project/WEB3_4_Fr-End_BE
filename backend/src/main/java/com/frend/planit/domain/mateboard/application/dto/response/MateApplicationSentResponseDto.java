package com.frend.planit.domain.mateboard.application.dto.response;

public record MateApplicationSentResponseDto(
        Long applicationId,
        Long matePostId,
        String mateTitle,
        String mateContentPreview,
        String writerNickname,
        String writerProfileImage,
        String status // PENDING, ACCEPTED, REJECTED
) {
}
