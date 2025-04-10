package com.frend.planit.global.exception;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.global.GlobalTestController;
import com.frend.planit.global.GlobalTestController.GlobalTestRequest;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(GlobalTestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExceptionHandlerTest {

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean(name = "jpaAuditingHandler")
    Object jpaAuditingHandler;

    @MockitoBean(name = "jpaMappingContext")
    Object jpaMappingContext;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("1) 일반적인 경우")
    void t1_1() throws Exception {
        mockMvc.perform(get("/test/global/ok"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.age").value(1))
                .andExpect(jsonPath("$.email").value("email@email.com"));
    }

    @Test
    @DisplayName("2) 바디가 없는 경우")
    void t2_1() throws Exception {
        mockMvc.perform(get("/test/global/no-content"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("3) ServiceException 이 발생한 경우")
    void t3_1() throws Exception {
        mockMvc.perform(get("/test/global/service-exception"))
                .andDo(print())
                .andExpect(status().is(ErrorType.COMMON_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.COMMON_SERVER_ERROR.getMessage()));
    }

    @Test
    @DisplayName("4-1) RequestBody 검증에 성공한 경우")
    void t4_1() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");
        String requestBody = objectMapper.writeValueAsString(data);

        mockMvc.perform(get("/test/global/request-body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(data.getName()))
                .andExpect(jsonPath("$.age").value(data.getAge()))
                .andExpect(jsonPath("$.email").value(data.getEmail()));
    }

    @Test
    @DisplayName("4-2) RequestBody 검증에 실패한 경우")
    void t4_2() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("testing", 0, " ");
        String requestBody = objectMapper.writeValueAsString(data);

        mockMvc.perform(get("/test/global/request-body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "name", "value", data.getName(),
                                "message", GlobalTestController.NAME_LENGTH),
                        Map.of("field", "age", "value", data.getAge(),
                                "message", GlobalTestController.AGE_POSITIVE),
                        Map.of("field", "email", "value", data.getEmail(),
                                "message", GlobalTestController.EMAIL_NOT_BLANK)))
                );
    }

    @Test
    @DisplayName("4-3) RequestBody 파싱에 실패한 경우")
    void t4_3() throws Exception {
        Map<String, String> data = Map.of(
                "name", "test",
                "age", "age",
                "email", "test@email.com"
        );
        String requestBody = objectMapper.writeValueAsString(data);

        mockMvc.perform(get("/test/global/request-body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()));
    }

    @Test
    @DisplayName("5-1) ModelAttribute 검증에 성공한 경우")
    void t5_1() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");

        mockMvc.perform(get("/test/global/model-attribute")
                        .param("name", data.getName())
                        .param("age", String.valueOf(data.getAge()))
                        .param("email", data.getEmail()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(data.getName()))
                .andExpect(jsonPath("$.age").value(data.getAge()))
                .andExpect(jsonPath("$.email").value(data.getEmail()));
    }

    @Test
    @DisplayName("5-2) ModelAttribute 검증에 실패한 경우")
    void t5_2() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("testing", 0, " ");

        mockMvc.perform(get("/test/global/model-attribute")
                        .param("name", data.getName())
                        .param("age", String.valueOf(data.getAge()))
                        .param("email", data.getEmail()))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "name", "value", data.getName(),
                                "message", GlobalTestController.NAME_LENGTH),
                        Map.of("field", "age", "value", data.getAge(),
                                "message", GlobalTestController.AGE_POSITIVE),
                        Map.of("field", "email", "value", data.getEmail(),
                                "message", GlobalTestController.EMAIL_NOT_BLANK)))
                );
    }

    @Test
    @DisplayName("6-1) RequestParam 검증에 성공한 경우")
    void t6_1() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");

        mockMvc.perform(get("/test/global/request-param")
                        .param("name", data.getName())
                        .param("age", String.valueOf(data.getAge()))
                        .param("email", data.getEmail()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(data.getName()))
                .andExpect(jsonPath("$.age").value(data.getAge()))
                .andExpect(jsonPath("$.email").value(data.getEmail()));
    }

    @Test
    @DisplayName("6-2) RequestParam 검증에 실패한 경우")
    void t6_2() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("testing", 0, " ");

        mockMvc.perform(get("/test/global/request-param")
                        .param("name", data.getName())
                        .param("age", String.valueOf(data.getAge()))
                        .param("email", data.getEmail()))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "name", "value", data.getName(),
                                "message", GlobalTestController.NAME_LENGTH),
                        Map.of("field", "age", "value", data.getAge(),
                                "message", GlobalTestController.AGE_POSITIVE),
                        Map.of("field", "email", "value", data.getEmail(),
                                "message", GlobalTestController.EMAIL_NOT_BLANK)))
                );
    }

    @Test
    @DisplayName("6-3) RequestParam 타입이 일치하지 않는 경우")
    void t6_3() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");
        String notInt = "age";

        mockMvc.perform(get("/test/global/request-param")
                        .param("name", data.getName())
                        .param("age", notInt)
                        .param("email", data.getEmail()))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", hasSize(1)))
                .andExpect(jsonPath("$.details[0].field").value("age"))
                .andExpect(jsonPath("$.details[0].value").value(notInt))
                .andExpect(jsonPath("$.details[0].message")
                        .value("int 타입으로 변환할 수 없습니다."));
    }

    @Test
    @DisplayName("6-4) RequestParam 이 누락되었을 경우")
    void t6_4() throws Exception {
        mockMvc.perform(get("/test/global/request-param"))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", hasItem(anyOf(
                        allOf(
                                hasEntry("field", "name"),
                                hasEntry("value", null),
                                hasEntry("message", "String 타입의 요청 파라미터가 누락되었습니다.")
                        ),
                        allOf(
                                hasEntry("field", "age"),
                                hasEntry("value", null),
                                hasEntry("message", "int 타입의 요청 파라미터가 누락되었습니다.")
                        ),
                        allOf(
                                hasEntry("field", "email"),
                                hasEntry("value", null),
                                hasEntry("message", "String 타입의 요청 파라미터가 누락되었습니다.")
                        )
                ))));
    }


    @Test
    @DisplayName("7-1) PathVariable 검증에 성공한 경우")
    void t7_1() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");

        mockMvc.perform(get("/test/global/path-variable/{name}/{age}/{email}",
                        data.getName(), data.getAge(), data.getEmail()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(data.getName()))
                .andExpect(jsonPath("$.age").value(data.getAge()))
                .andExpect(jsonPath("$.email").value(data.getEmail()));
    }

    @Test
    @DisplayName("7-2) PathVariable 검증에 실패한 경우")
    void t7_2() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("testing", 0, " ");

        mockMvc.perform(get("/test/global/path-variable/{name}/{age}/{email}",
                        data.getName(), data.getAge(), data.getEmail()))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                        Map.of("field", "name", "value", data.getName(),
                                "message", GlobalTestController.NAME_LENGTH),
                        Map.of("field", "age", "value", data.getAge(),
                                "message", GlobalTestController.AGE_POSITIVE),
                        Map.of("field", "email", "value", data.getEmail(),
                                "message", GlobalTestController.EMAIL_NOT_BLANK)))
                );
    }

    @Test
    @DisplayName("7-3) PathVariable 타입이 일치하지 않는 경우")
    void t7_3() throws Exception {
        GlobalTestRequest data = new GlobalTestRequest("test", 123, "test@email.com");
        String notInt = "age";

        mockMvc.perform(get("/test/global/path-variable/{name}/{age}/{email}",
                        data.getName(), notInt, data.getEmail()))
                .andDo(print())
                .andExpect(status().is(ErrorType.REQUEST_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.REQUEST_NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.details", hasSize(1)))
                .andExpect(jsonPath("$.details[0].field").value("age"))
                .andExpect(jsonPath("$.details[0].value").value(notInt))
                .andExpect(jsonPath("$.details[0].message")
                        .value("int 타입으로 변환할 수 없습니다."));
    }

    @Test
    @DisplayName("7-4) PathVariable 이 누락되었을 경우")
    void t7_4() throws Exception {
        mockMvc.perform(get("/test/global/missing-path-variable"))
                .andDo(print())
                .andExpect(status().is(ErrorType.MISSING_PATH_VARIABLE.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.MISSING_PATH_VARIABLE.getMessage()))
                .andExpect(jsonPath("$.details", hasItem(anyOf(
                        allOf(
                                hasEntry("field", "name"),
                                hasEntry("value", null),
                                hasEntry("message", "해당 경로 파라미터가 누락되었습니다.")
                        ),
                        allOf(
                                hasEntry("field", "age"),
                                hasEntry("value", null),
                                hasEntry("message", "해당 경로 파라미터가 누락되었습니다.")
                        ),
                        allOf(
                                hasEntry("field", "email"),
                                hasEntry("value", null),
                                hasEntry("message", "해당 경로 파라미터가 누락되었습니다.")
                        )
                ))));
    }

    @Test
    @DisplayName("8) HTTP 메서드가 일치하지 않을 경우")
    void t8_1() throws Exception {
        mockMvc.perform(post("/test/global/ok"))
                .andDo(print())
                .andExpect(status().is(ErrorType.METHOD_NOT_ALLOWED.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    @DisplayName("9) 경로가 존재하지 않을 경우")
    void t9_1() throws Exception {
        String wrongPath = "/test/global/wrong-path";

        mockMvc.perform(get(wrongPath))
                .andDo(print())
                .andExpect(status().is(ErrorType.NO_RESOURCE_FOUND.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.NO_RESOURCE_FOUND.getMessage()))
                .andExpect(jsonPath("$.details", hasSize(1)))
                .andExpect(jsonPath("$.details[0].field").value("path"))
                .andExpect(jsonPath("$.details[0].value").value(wrongPath.substring(1)))
                .andExpect(jsonPath("$.details[0].message")
                        .value("해당 경로가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("10) 기타 예외가 발생한 경우")
    void t10_1() throws Exception {
        mockMvc.perform(get("/test/global/runtime-exception"))
                .andDo(print())
                .andExpect(status().is(ErrorType.COMMON_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorType.COMMON_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.details", hasSize(1)))
                .andExpect(jsonPath("$.details[0].field").value("cause"))
                .andExpect(jsonPath("$.details[0].value").value(RuntimeException.class.getName()))
                .andExpect(jsonPath("$.details[0].message")
                        .value(ErrorType.COMMON_SERVER_ERROR.getMessage()));
    }
}