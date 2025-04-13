package com.frend.planit.domain.mateboard.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MateRejectRequest(
        @Schema(description = "신청 ID", example = "3")
        Long applicationId,

        @Schema(description = "메이트 게시글 ID", example = "1")
        Long matePostId
) {

}