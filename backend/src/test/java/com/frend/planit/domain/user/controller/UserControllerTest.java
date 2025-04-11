package com.frend.planit.domain.user.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.TestConfig;
import com.frend.planit.domain.calendar.dto.response.CalendarActivityResponseDto;
import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.service.MateService;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.service.UserService;
import com.frend.planit.global.security.JwtAuthenticationFilter;
import com.frend.planit.testsecurity.WithMockCustomUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {UserController.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MateService mateService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("최초 로그인 추가 정보 입력 API")
    void updateFirstInfo() throws Exception {
        Long mockUserId = 1L;

        UserFirstInfoRequest request = new UserFirstInfoRequest(
                "test@email.com",
                "닉네임",
                "01012345678",
                LocalDate.of(1998, 1, 1),
                Gender.MALE
        );

        doNothing().when(userService).updateFirstInfo(mockUserId, request);

        mockMvc.perform(patch("/api/v1/user/me/first-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("닉네임 중복 확인 API")
    void checkNickname() throws Exception {
        when(userService.isNicknameAvailable("닉네임")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("이메일 중복 확인 API")
    void checkEmail() throws Exception {
        when(userService.isEmailAvailable("test@email.com")).thenReturn(false);

        mockMvc.perform(get("/api/v1/user/check-email")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("휴대폰 번호 중복 확인 API")
    void checkPhone() throws Exception {
        when(userService.isPhoneAvailable("010-1234-5678")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-phone")
                        .param("phone", "010-1234-5678"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "사용자1")
    @DisplayName("내 정보 조회 API")
    void getMyInfo() throws Exception {
        Long mockUserId = 1L;

        UserMeResponse response = UserMeResponse.builder()
                .id(mockUserId)
                .nickname("닉네임")
                .email("test@email.com")
                .phone("010-1234-5678")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1998, 1, 1))
                .profileImage("https://image.test.com/profile.png")
                .bio("자기소개입니다.")
                .status(UserStatus.REGISTERED)
                .createdAt(LocalDateTime.now())
                .socialType(SocialType.GOOGLE)
                .mailingType(true)
                .role(Role.USER)
                .build();

        when(userService.getMyInfo(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.birthDate").value("1998-01-01"))
                .andExpect(jsonPath("$.profileImage").value("https://image.test.com/profile.png"))
                .andExpect(jsonPath("$.bio").value("자기소개입니다."))
                .andExpect(jsonPath("$.status").value("REGISTERED"))
                .andExpect(jsonPath("$.socialType").value("GOOGLE"))
                .andExpect(jsonPath("$.mailingType").value(true))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "사용자1")
    @DisplayName("나의 활동 내역 조회 API")
    void getUserActivity() throws Exception {
        Long mockUserId = 1L;

        MateResponseDto mateResponseDto = MateResponseDto.builder()
                .matePostId(1L)
                .authorId(mockUserId)
                .title("여행 동행 모집")
                .content("같이 여행 갈 사람 구합니다.")
                .createdAt(LocalDateTime.now())
                .nickname("사용자1")
                .imageUrl("https://image.test.com/post.png")
                .build();

        when(userService.getUserActivity(mockUserId)).thenReturn(List.of(mateResponseDto));

        mockMvc.perform(get("/api/v1/user/me/activity/mate-post")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matePostId").value(1L))
                .andExpect(jsonPath("$[0].title").value("여행 동행 모집"))
                .andExpect(jsonPath("$[0].content").value("같이 여행 갈 사람 구합니다."))
                .andExpect(jsonPath("$[0].nickname").value("사용자1"))
                .andExpect(jsonPath("$[0].imageUrl").value("https://image.test.com/post.png"))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "사용자1")
    @DisplayName("내가 작성한 메이트 댓글 목록 조회 API")
    void getUserMateComments() throws Exception {
        Long mockUserId = 1L;

        // mock 댓글 응답 DTO 생성
        MateCommentResponseDto commentDto = MateCommentResponseDto.builder()
                .mateCommentId(10L)
                .matePostId(100L)
                .authorId(mockUserId)
                .nickname("사용자1")
                .profileImageUrl("https://image.test.com/profile.png")
                .content("같이 여행가요!")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // mock 설정
        when(userService.getUserCommentsActivity(mockUserId)).thenReturn(List.of(commentDto));

        // API 호출 및 응답 검증
        mockMvc.perform(get("/api/v1/user/me/activity/mate-comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mateCommentId").value(10L))
                .andExpect(jsonPath("$[0].matePostId").value(100L))
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andExpect(jsonPath("$[0].nickname").value("사용자1"))
                .andExpect(jsonPath("$[0].profileImageUrl").value(
                        "https://image.test.com/profile.png"))
                .andExpect(jsonPath("$[0].content").value("같이 여행가요!"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].modifiedAt").exists());
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "사용자1")
    @DisplayName("내가 참여한 캘린더 목록 조회 API")
    void getUserCalendars() throws Exception {
        Long mockUserId = 1L;

        CalendarActivityResponseDto calendar1 = CalendarActivityResponseDto.builder()
                .calendarId(1L)
                .calendarTitle("제주 여행")
                .startDate(LocalDateTime.of(2025, 5, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 5, 3, 18, 0))
                .note("친구랑 제주도")
                .sharedUserNicknames(List.of("사용자1", "친구1"))
                .build();

        CalendarActivityResponseDto calendar2 = CalendarActivityResponseDto.builder()
                .calendarId(2L)
                .calendarTitle("부산 여행")
                .startDate(LocalDateTime.of(2025, 6, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 6, 3, 18, 0))
                .note("해운대에서 놀자")
                .sharedUserNicknames(List.of("사용자1", "친구2"))
                .build();

        when(userService.getUserCalendarActivity(mockUserId)).thenReturn(
                List.of(calendar1, calendar2));

        mockMvc.perform(get("/api/v1/user/me/activity/calendars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calendarId").value(1L))
                .andExpect(jsonPath("$[0].calendarTitle").value("제주 여행"))
                .andExpect(jsonPath("$[0].note").value("친구랑 제주도"))
                .andExpect(jsonPath("$[0].sharedUserNicknames[0]").value("사용자1"))
                .andExpect(jsonPath("$[0].sharedUserNicknames[1]").value("친구1"))
                .andExpect(jsonPath("$[1].calendarId").value(2L))
                .andExpect(jsonPath("$[1].calendarTitle").value("부산 여행"))
                .andExpect(jsonPath("$[1].note").value("해운대에서 놀자"))
                .andExpect(jsonPath("$[1].sharedUserNicknames[0]").value("사용자1"))
                .andExpect(jsonPath("$[1].sharedUserNicknames[1]").value("친구2"));
    }

}
