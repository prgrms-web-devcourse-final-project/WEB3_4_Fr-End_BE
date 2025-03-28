package com.frend.planit.domain.mateboard.post.dto.request;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 메이트 모집 게시글 등록, 수정 시에 사용하는 Request DTO입니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
public class MateRequestDto {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    @NotNull(message = "여행 지역을 선택해 주세요.")
    private TravelRegion travelRegion;

    @NotNull(message = "여행 시작일을 선택해 주세요.")
    private LocalDate travelStartDate;

    @NotNull(message = "여행 종료일을 선택해 주세요.")
    private LocalDate travelEndDate;

    @NotNull(message = "메이트 성별을 선택해 주세요.")
    private MateGender mateGender;


}
