package com.frend.planit.domain.mateboard.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.mateboard.comment.dto.request.MateCommentRequestDto;
import com.frend.planit.testsecurity.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@Sql(scripts = "/sql/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
class MateCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("메이트 댓글 작성 성공")
    @WithMockCustomUser(id = 2L)
    void createComment() throws Exception {
        MateCommentRequestDto request = new MateCommentRequestDto("댓글 작성 테스트입니다.");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/mate-board/posts/{mateId}/comments", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
//
//    @Test
//    @DisplayName("메이트 댓글 수정 성공")
//    @WithMockCustomUser(id = 2L)
//    void updateComment() throws Exception {
//        MateCommentRequestDto updateRequest = new MateCommentRequestDto("수정된 댓글 내용");
//        String json = objectMapper.writeValueAsString(updateRequest);
//
//        mockMvc.perform(put("/api/v1/mate-board/posts/{mateId}/comments/{id}", 5L, 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("수정된 댓글 내용")));
//    }
//
//    @Test
//    @DisplayName("메이트 댓글 삭제 성공")
//    @WithMockCustomUser(id = 2L)
//    void deleteComment() throws Exception {
//        mockMvc.perform(delete("/api/v1/mate-board/posts/{mateId}/comments/{id}", 5L, 1L))
//                .andExpect(status().isNoContent());
//
//    }
//
//    @Test
//    @DisplayName("메이트 댓글 목록 조회 성공")
//    @WithMockCustomUser(id = 3L)
//        // 로그인만 되어 있으면 조회는 가능
//    void getComments() throws Exception {
//        mockMvc.perform(get("/api/v1/mate-board/posts/{mateId}/comments", 5L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1)); // ← 여기가 핵심
//    }
//
//
//    @Test
//    @DisplayName("본인이 아닌 유저가 댓글 수정 시 실패")
//    @WithMockCustomUser(id = 3L)
//    void updateComment_NotWriter_ShouldFail() throws Exception {
//        MateCommentRequestDto updateRequest = new MateCommentRequestDto("수정 시도");
//        String json = objectMapper.writeValueAsString(updateRequest);
//
//        mockMvc.perform(put("/api/v1/mate-board/posts/5/comments/{commentId}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("본인이 아닌 유저가 댓글 삭제 시 실패")
//    @WithMockCustomUser(id = 3L)
//    void deleteComment_NotWriter_ShouldFail() throws Exception {
//        mockMvc.perform(delete("/api/v1/mate-board/posts/5/comments/{commentId}", 1L))
//                .andExpect(status().isForbidden());
//    }

}
