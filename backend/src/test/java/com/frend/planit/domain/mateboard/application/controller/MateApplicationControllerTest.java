package com.frend.planit.domain.mateboard.application.controller;

import static com.frend.planit.domain.mateboard.application.entity.MateApplicationStatus.ACCEPTED;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import com.frend.planit.testsecurity.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;


@AutoConfigureMockMvc
@SpringBootTest
@Sql(scripts = "/sql/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MateApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MateRepository mateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("메이트 게시글 모집 신청 성공")
    @WithMockCustomUser
    void applyToMatePost() throws Exception {
        Long matePostId = 25L;
        mockMvc.perform(post("/api/v1/mate-board/applications/{mateId}", matePostId))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(""))); // 혹은 응답 JSON 검사
    }

//    @Test
//    @DisplayName("메이트 모집 신청 취소")
//    @WithMockCustomUser(id = 1)
//    void cancelMateApplication() throws Exception {
//        Long mateId = 4L;
//
//        mockMvc.perform(delete("/api/v1/mate-board/applications/{mateId}", mateId))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("")));
//    }

    @Test
    @DisplayName("메이트 신청 수락 성공")
    @WithMockCustomUser(id = 2L)
        // 작성자 ID
    void acceptApplication() throws Exception {
        Long mateId = 4L;
        Long applicantId = 1L;

        mockMvc.perform(
                        put("/api/v1/mate-board/applications/{mateId}/accept/{applicantId}", mateId,
                                applicantId)
                                .param("userId", applicantId.toString())) // 또는 requestBody or pathVariable
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }

    @Test
    @DisplayName("메이트 신청 거절 성공")
    @WithMockCustomUser(id = 2L)
        // 게시글 작성자 ID
    void rejectMateApplication() throws Exception {
        Long mateId = 4L;        // 게시글 ID
        Long applicantId = 1L;   // 신청자 ID

        mockMvc.perform(put("/api/v1/mate-board/applications/{mateId}/reject/{applicantId}", mateId,
                        applicantId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }

    @Test
    @DisplayName("모집 인원 초과 시 신청 불가")
    @WithMockCustomUser(id = 4L) // 4번 유저가 새로 신청 시도
    @Sql(scripts = "/sql/init-full-applications.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void applyToMatePost_whenFull_thenFail() throws Exception {
        Long mateId = 5L; // 모집 인원 3명, 이미 3명 수락된 상태

        mockMvc.perform(post("/api/v1/mate-board/applications/{mateId}", mateId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("모집 인원이 초과되었습니다.")));
    }

    @Test
    @DisplayName("모집 인원이 다 찼을 경우 모집 상태 자동 종료")
    @WithMockCustomUser(id = 1L) // 게시글 작성자
    @Sql(scripts = "/sql/init-full-applications.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void closeRecruitmentWhenFull() throws Exception {
        Long mateId = 5L;

        // 1. 1명 수락
        mockMvc.perform(
                        put("/api/v1/mate-board/applications/{mateId}/accept/{applicantId}", mateId, 2L))
                .andExpect(status().isOk());

        // 2. 2명 수락
        mockMvc.perform(
                        put("/api/v1/mate-board/applications/{mateId}/accept/{applicantId}", mateId, 3L))
                .andExpect(status().isOk());

        // 3. 3명 수락 - 최종 수락, 상태 CLOSE
        mockMvc.perform(
                        put("/api/v1/mate-board/applications/{mateId}/accept/{applicantId}", mateId, 4L))
                .andExpect(status().isOk());

        // 여기서 모집 상태가 자동으로 CLOSE 되었는지 검증
        Mate mate = mateRepository.findById(mateId).orElseThrow();
        assertEquals(RecruitmentStatus.CLOSED, mate.getRecruitmentStatus());
    }

    @Test
    void closeRecruitmentWhenTravelStarts() throws Exception {
        // given
        Long mateId = 5L;

        // 테스트용 모집글 travel_start_date = 오늘보다 하루 전으로 세팅되어 있어야 함
        // ex) INSERT 시 '2025-04-07'처럼 넣거나, 혹은 서비스에서 강제로 세팅

        // when: 현재 날짜 기준으로 모집 상태를 검사하는 메서드를 호출
        // (직접 호출하거나, 해당 조건이 반영되는 API 호출)

        // then: 모집 상태가 CLOSED로 바뀌었는지 확인
        Mate mate = mateRepository.findById(mateId).orElseThrow();
        assertEquals(RecruitmentStatus.CLOSED, mate.getRecruitmentStatus());
    }

    @Test
    @Transactional
    @Sql(scripts = "/sql/init-full-applications.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getMateBoardRecruitmentInfo() throws Exception {
        // given
        Long mateId = 5L;

        // when
        MvcResult result = mockMvc.perform(get("/api/v1/mate-board/posts/{mateId}", mateId))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(responseJson);

        int recruitCount = root.path("recruitCount").asInt();
        String recruitmentStatus = root.path("recruitmentStatus").asText();

        // 리포지토리로 모집글을 다시 가져와서 내부에서 수락된 신청자 수 확인
        Mate mate = mateRepository.findById(mateId).orElseThrow();
        long acceptedCount = mate.getApplications().stream()
                .filter(app -> app.getStatus() == ACCEPTED) // static import 하면 더 깔끔
                .count();

        long appliedCount = mate.getApplications().size(); // 추가!

        assertEquals(3, recruitCount);
        assertEquals("OPEN", recruitmentStatus); // 혹은 "CLOSED"면 거기에 맞게
        assertEquals(3, appliedCount);
        assertEquals(0, acceptedCount);
    }

    @Test
    @DisplayName("모집 마감된 게시글에 신청하면 실패")
    @WithMockCustomUser(id = 6L) // 신청자
    @Sql(scripts = "/sql/init-full-applications.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void applyToClosedMatePost_shouldFail() throws Exception {
        // given
        Long mateId = 5L;

        // when & then
        mockMvc.perform(post("/api/v1/mate-board/applications/{mateId}", mateId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("이미 모집이 종료된 게시글입니다.")));
    }
//
//    @Test
//    @DisplayName("여행 시작일이 지나면 모집 상태 자동 종료")
//    @WithMockCustomUser(id = 1L)
//    @Sql(scripts = "/sql/init-expired-mate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    void closeRecruitmentWhenTravelStarts_shouldSucceed() throws Exception {
//        // given
//        Long mateId = 10L;
//
//        // when
//        Mate mate = mateRepository.findById(mateId).orElseThrow();
//
//        // then
//        assertEquals(RecruitmentStatus.CLOSED, mate.getRecruitmentStatus());
//    }

    
}
