package com.frend.planit.domain.mateboard.post.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.mateboard.post.dto.request.MateRequestDto;
import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.testsecurity.WithMockCustomUser;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


@Sql(scripts = {"/sql/init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class MateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("게시글 생성 API 테스트")
    @Test
    @WithMockCustomUser(id = 1L, username = "mockuser")
    void createMate_success() throws Exception {
        // given
        MateRequestDto requestDto = MateRequestDto.builder()
                .title("테스트 제목입니다")
                .content("테스트 내용입니다. 20자 이상 입력해야 해서 울릉도 동남쪽 뱃길따라 이백리 외로운 섬하나 새들의 고향.")
                .recruitCount(3)
                .travelRegion(TravelRegion.ALL)
                .travelStartDate(LocalDate.of(2025, 05, 01))
                .travelEndDate(LocalDate.of(2025, 05, 30))
                .mateGender(MateGender.NO_PREFERENCE)
                .build();

        String json = objectMapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/api/v1/mate-board/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber()); // 생성된 게시글 ID나 내용
    }

    @Test
    @DisplayName("메이트 게시글 단건 조회 성공")
    void getMateBoard() throws Exception {
        // given
        Long postId = 1L;

        // when & then
        mockMvc.perform(get("/api/v1/mate-board/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists());
    }

    @DisplayName("메이트 게시글 목록 조회")
    @Test
    @WithMockCustomUser(id = 1L, username = "mockuser")
    void getMatesList_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/mate-board/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").exists());
    }

    @Test
    @DisplayName("검색어로 메이트 게시글 목록 조회 - 제목 기준")
    void getMatesList_byKeywordInTitle_success() throws Exception {

        UserDetails testUser = User.builder()
                .username("mock@user.com")
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(get("/api/v1/mate-board/posts")
                        .param("keyword", "서울")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].title").value(hasItem(containsString("서울"))));
    }

    @Test
    @DisplayName("모집 상태 OPEN 필터링 조회")
    void getMatesList_byStatusOpen_success() throws Exception {

        UserDetails testUser = User.builder()
                .username("mock@user.com")
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(get("/api/v1/mate-board/posts")
                        .param("status", "OPEN")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data[*].recruitmentStatus").value(everyItem(equalTo("OPEN"))));
    }

    @Test
    @DisplayName("지역 JEJU 필터링 조회")
    void getMatesList_byRegionJeju_success() throws Exception {

        UserDetails testUser = User.builder()
                .username("mock@user.com")
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(get("/api/v1/mate-board/posts")
                        .param("region", "JEJU")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].travelRegion").value(everyItem(equalTo("JEJU"))));
    }

    @Test
    @DisplayName("모든 필터 미적용 시 전체 목록 조회")
    void getMatesList_noFilter_success() throws Exception {

        UserDetails testUser = User.builder()
                .username("mock@user.com")
                .password("password")
                .roles("USER")
                .build();

        mockMvc.perform(get("/api/v1/mate-board/posts")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("메이트 게시글 수정 성공")
    @WithMockCustomUser
        // 로그인 사용자 설정
    void updateMateBoard() throws Exception {
        // given
        Long postId = 3L;
        MateRequestDto request = MateRequestDto.builder()
                .title("수정된 제목")
                .content("수정된 내용입니다. 충분히 길게 작성해야 합니다.") // 20자 이상
                .recruitCount(4)
                .travelRegion(TravelRegion.SEOUL)
                .travelStartDate(LocalDate.now().plusDays(10))
                .travelEndDate(LocalDate.now().plusDays(15))
                .mateGender(MateGender.FEMALE)
                .build();

        // when & then
        mockMvc.perform(put("/api/v1/mate-board/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 내용입니다. 충분히 길게 작성해야 합니다."));
    }

    @Test
    @DisplayName("메이트 게시글 삭제 성공")
    @WithMockCustomUser
        // 로그인 사용자 설정
    void deleteMateBoard() throws Exception {
        // given
        Long postId = 1L;

        // when & then
        mockMvc.perform(delete("/api/v1/mate-board/posts/{id}", postId))
                .andExpect(status().isNoContent());
    }


}