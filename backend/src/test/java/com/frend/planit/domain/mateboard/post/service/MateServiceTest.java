package com.frend.planit.domain.mateboard.post.service;

import static com.frend.planit.domain.mateboard.post.entity.MateGender.NO_PREFERENCE;
import static com.frend.planit.domain.mateboard.post.entity.TravelRegion.SEOUL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.frend.planit.domain.mateboard.post.dto.request.MateRequestDto;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(scripts = "/sql/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Transactional
public class MateServiceTest {

    @Autowired
    private MateService mateService;

    @DisplayName("메이트 게시글 생성 - 성공")
    @Test
    void createMate_success() {
        // given
        Long userId = 1L;
        MateRequestDto mateRequestDto = MateRequestDto.builder()
                .title("제목입니다")
                .content("내용입니다")
                .travelRegion(SEOUL)
                .travelStartDate(LocalDate.of(2025, 5, 1))
                .travelEndDate(LocalDate.of(2025, 5, 10))
                .recruitCount(3)
                .mateGender(NO_PREFERENCE)
                .imageId(null)
                .build();

        // when
        Long savedId = mateService.createMate(userId, mateRequestDto);

        // then
        assertNotNull(savedId);
    }
    
}
