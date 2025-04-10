package com.frend.planit.domain.image;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.accommodation.loader.AccommodationInitialDataLoader;
import com.frend.planit.domain.image.dto.request.ImageTestRequest;
import com.frend.planit.domain.image.service.ImageService;
import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.global.response.ErrorType;
import java.util.stream.LongStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest()
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageIntegrationTest {

    @MockitoBean
    AccommodationInitialDataLoader loader; // TourApi 초기화 무력화

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ImageService imageService;

    ImageTestRequest testRequest = new ImageTestRequest(
            "testName",
            123,
            "testEmail",
            LongStream.rangeClosed(1, 10).boxed().toList()
    );

    @Test
    @Commit
    @DisplayName("1-1) 이미지 업로드")
    void t1_1() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/image")
                            .contentType("image/jpeg"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.presigned").exists());
        }
    }

    @Test
    @DisplayName("1-2) 이미지 업로드 실패, 확장자 미지원")
    void t1_2() throws Exception {
        mockMvc.perform(post("/api/v1/image")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorType.MIME_NOT_SUPPORTED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorType.MIME_NOT_SUPPORTED.getMessage()));
    }

    @Test
    @Commit
    @DisplayName("2-1) 게시글 등록")
    void t2_1() throws Exception {
        String requestBody = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(post("/test/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));

        Assertions.assertThat(imageService.getAllImages(HolderType.TEST, 1).count()).isEqualTo(10);
    }

    @Test
    @DisplayName("2-2) 게시글 등록 실패, 존재하지 않는 이미지 ID")
    void t2_2() throws Exception {
        ImageTestRequest wrongTestRequest = new ImageTestRequest(
                testRequest.name(),
                testRequest.age(),
                testRequest.email(),
                LongStream.rangeClosed(11, 13).boxed().toList()
        );
        String requestBody = objectMapper.writeValueAsString(wrongTestRequest);

        mockMvc.perform(post("/test/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(ErrorType.IMAGE_UPLOAD_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorType.IMAGE_UPLOAD_FAILED.getMessage()));
    }

    @Test
    @DisplayName("3-1) 게시글 조회")
    void t3_1() throws Exception {
        mockMvc.perform(get("/test/image/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testRequest.name()))
                .andExpect(jsonPath("$.age").value(testRequest.age()))
                .andExpect(jsonPath("$.email").value(testRequest.email()))
                .andExpect(jsonPath("$.imageData.count").value(testRequest.imageIds().size()))
                .andExpect(jsonPath("$.imageData.imageIds").isArray())
                .andExpect(jsonPath("$.imageData.imageUrls").isArray());
    }

    @Test
    @DisplayName("3-2) 게시글 조회 실패, 존재하지 않는 게시글 ID")
    void t3_2() throws Exception {
        mockMvc.perform(get("/test/image/{id}", 2))
                .andDo(print())
                .andExpect(status().is(ErrorType.COMMON_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorType.COMMON_SERVER_ERROR.getMessage()));
    }

    @Test
    @Commit
    @DisplayName("4-1) 이미지 변경")
    void t4_1() throws Exception {
        testRequest = new ImageTestRequest(
                "testName2",
                456,
                "testEmail2",
                LongStream.rangeClosed(3, 7).boxed().toList()
        );

        String requestBody = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(patch("/test/image/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testRequest.name()))
                .andExpect(jsonPath("$.age").value(testRequest.age()))
                .andExpect(jsonPath("$.email").value(testRequest.email()))
                .andExpect(jsonPath("$.imageData.count").value(testRequest.imageIds().size()))
                .andExpect(jsonPath("$.imageData.imageIds").isArray())
                .andExpect(jsonPath("$.imageData.imageUrls").isArray());

        Assertions.assertThat(imageService.getAllImages(HolderType.TEST, 1).count()).isEqualTo(5);
    }

    @Test
    @DisplayName("4-2) 이미지 변경 실패, 존재하지 않는 이미지 ID")
    void t4_2() throws Exception {
        ImageTestRequest wrongTestRequest = new ImageTestRequest(
                testRequest.name(),
                testRequest.age(),
                testRequest.email(),
                LongStream.rangeClosed(3, 13).boxed().toList()
        );

        String requestBody = objectMapper.writeValueAsString(wrongTestRequest);

        mockMvc.perform(patch("/test/image/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(ErrorType.IMAGE_UPLOAD_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorType.IMAGE_UPLOAD_FAILED.getMessage()));
    }

    @Test
    @DisplayName("4-3) 변경된 게시글 조회")
    void t4_3() throws Exception {
        mockMvc.perform(get("/test/image/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testRequest.name()))
                .andExpect(jsonPath("$.age").value(testRequest.age()))
                .andExpect(jsonPath("$.email").value(testRequest.email()))
                .andExpect(jsonPath("$.imageData.count").value(testRequest.imageIds().size()))
                .andExpect(jsonPath("$.imageData.imageIds").isArray())
                .andExpect(jsonPath("$.imageData.imageUrls").isArray());
    }

    @Test
    @Commit
    @DisplayName("5-1) 게시글 삭제")
    void t5_1() throws Exception {
        mockMvc.perform(delete("/test/image/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertThat(imageService.getAllImages(HolderType.TEST, 1).count()).isEqualTo(0);
    }

    @Test
    @Commit
    @DisplayName("5-2) 고아 이미지 삭제")
    void t5_2() throws Exception {
        Assertions.assertThat(imageService.cleanImages()).isEqualTo(10);
    }
}