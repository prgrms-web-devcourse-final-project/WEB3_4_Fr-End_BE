package com.frend.planit.global;

import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ApiResponseHelper;
import com.frend.planit.global.response.ErrorType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@Validated
@RestController
@RequestMapping("/test/global")
public class GlobalTestController {

    public final static String NAME_NOT_BLANK = "이름은 필수입니다.";
    public final static String NAME_LENGTH = "이름은 1자 이상 5자 이하입니다.";
    public final static String AGE_POSITIVE = "나이는 1 이상입니다.";
    public final static String EMAIL_NOT_BLANK = "이메일은 필수입니다.";

    @GetMapping("/ok")
    public ResponseEntity<GlobalTestResponse> ok() {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse("name", 1, "email@email.com"));
    }

    @GetMapping("/no-content")
    public ResponseEntity<GlobalTestResponse> noContent() {
        return ApiResponseHelper.success(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/custom-code")
    public ResponseEntity<GlobalTestResponse> customCode() {
        return ApiResponseHelper.success(299,
                new GlobalTestResponse("name", 1, "email@email.com"));
    }

    @GetMapping("/service-exception")
    public ResponseEntity<GlobalTestResponse> serviceException() {
        throw new ServiceException(ErrorType.GLOBAL_TEST_CODE);
    }

    @GetMapping("/runtime-exception")
    public ResponseEntity<Void> runtimeException() {
        throw new RuntimeException(ErrorType.GLOBAL_TEST_CODE.getMessage());
    }

    @GetMapping("/request-body")
    public ResponseEntity<GlobalTestResponse> requestBody(
            @RequestBody @Valid GlobalTestRequest request) {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse(request.name, request.age, request.email));
    }

    @GetMapping("/model-attribute")
    public ResponseEntity<GlobalTestResponse> modelAttribute(
            @ModelAttribute @Valid GlobalTestRequest request) {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse(request.name, request.age, request.email));
    }

    @GetMapping("/request-param")
    public ResponseEntity<GlobalTestResponse> requestParam(
            @RequestParam @NotBlank(message = NAME_NOT_BLANK) @Length(min = 1, max = 5, message = NAME_LENGTH)
            String name,
            @RequestParam @Positive(message = AGE_POSITIVE)
            int age,
            @RequestParam @NotBlank(message = EMAIL_NOT_BLANK)
            String email
    ) {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse(name, age, email));
    }

    @GetMapping("/path-variable/{name}/{age}/{email}")
    public ResponseEntity<GlobalTestResponse> pathVariable(
            @PathVariable @NotBlank(message = NAME_NOT_BLANK) @Length(min = 1, max = 5, message = NAME_LENGTH)
            String name,
            @PathVariable @Positive(message = AGE_POSITIVE)
            int age,
            @PathVariable @NotBlank(message = EMAIL_NOT_BLANK)
            String email
    ) {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse(name, age, email));
    }

    @GetMapping("/missing-path-variable")
    public ResponseEntity<GlobalTestResponse> missingPathVariable(
            @PathVariable @NotBlank(message = NAME_NOT_BLANK) @Length(min = 1, max = 5, message = NAME_LENGTH)
            String name,
            @PathVariable @Positive(message = AGE_POSITIVE)
            int age,
            @PathVariable @NotBlank(message = EMAIL_NOT_BLANK)
            String email
    ) {
        return ApiResponseHelper.success(HttpStatus.OK,
                new GlobalTestResponse(name, age, email));
    }

    public static class GlobalTestResponse {

        private String name;
        private int age;
        private String email;

        public GlobalTestResponse(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class GlobalTestRequest {

        @NotBlank(message = NAME_NOT_BLANK)
        @Length(min = 1, max = 5, message = NAME_LENGTH)
        private String name;

        @Positive(message = AGE_POSITIVE)
        private int age;

        @NotBlank(message = EMAIL_NOT_BLANK)
        private String email;

        public GlobalTestRequest(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }
    }
}