package com.frend.planit.domain.mateboard.post.dto.request;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

/**
 * 메이트 모집 게시글 등록, 수정 시에 사용하는 Request DTO입니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
@Builder
@Getter
public class MateRequestDto {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(min = 20, max = 2000, message = "내용은 20자 이상 2000자 이하로 입력해 주세요.")
    private String content;

    @NotNull(message = "모집 인원을 입력해 주세요.")
    @Min(value = 1, message = "모집 인원은 최소 1명 이상이어야 합니다.")
    private Integer recruitCount;

    @NotNull(message = "여행 지역을 선택해 주세요.")
    private TravelRegion travelRegion;

    @NotNull(message = "여행 시작일을 선택해 주세요.")
    @FutureOrPresent(message = "여행 시작일은 오늘보다 이전일 수 없습니다.")
    private LocalDate travelStartDate;

    @NotNull(message = "여행 종료일을 선택해 주세요.")
    private LocalDate travelEndDate;

    @NotNull(message = "메이트 성별을 선택해 주세요.")
    private MateGender mateGender;

    private Long imageId;

    /**
     * 여행 종료일이 시작일 이후이거나 같은 날짜인지 검증합니다. 유효성 검사 애너테이션(@AssertTrue)을 통해 자동으로 호출됩니다.
     */
    @AssertTrue(message = "여행 종료일은 여행 시작일 이후여야 합니다.")
    public boolean isTravelEndDateValid() {
        if (travelStartDate == null || travelEndDate == null) {
            return true;
        }
        return travelEndDate.isAfter(travelStartDate) || travelEndDate.isEqual(travelStartDate);
    }
}
