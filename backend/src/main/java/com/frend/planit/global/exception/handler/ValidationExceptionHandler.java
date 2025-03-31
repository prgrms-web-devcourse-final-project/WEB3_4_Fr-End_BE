package com.frend.planit.global.exception.handler;

import com.frend.planit.global.response.ErrorResponse;
import com.frend.planit.global.response.ErrorResponse.Detail;
import com.frend.planit.global.response.ErrorType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/*
 * 유효성 검사 예외 처리를 위한 핸들러 클래스입니다.
 */
@Order(2)
@RestControllerAdvice
public class ValidationExceptionHandler {

    // RequestBody, ModelAttribute 의 유효성 검사 예외를 처리합니다.
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        List<Detail> details = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new Detail(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(ErrorType.ARGUMENT_BINDING_ERROR.getCode())
                .body(new ErrorResponse(ErrorType.ARGUMENT_BINDING_ERROR.getMessage(), details));
    }

    // RequestParam, PathVariable 혹은 서비스 계층의 유효성 검사 예외를 처리합니다.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        // 예외 발생 계층 분석 및 에러 정보 추출
        String className = violations.iterator().next().getRootBeanClass().getName();
        List<Detail> details = violations.stream()
                .map(violation -> {
                    String fullPath = violation.getPropertyPath().toString();
                    String field = fullPath.contains(".")
                            ? fullPath.substring(fullPath.lastIndexOf('.') + 1)
                            : fullPath;

                    return new Detail(
                            field,
                            violation.getInvalidValue(),
                            violation.getMessage());
                })
                .toList();

        // 컨트롤러 계층일 경우 잘못된 요청으로 판단
        if (className.contains("controller") || className.contains("Controller")) {
            return ResponseEntity
                    .status(ErrorType.CONSTRAINT_VIOLATION.getCode())
                    .body(new ErrorResponse(ErrorType.CONSTRAINT_VIOLATION.getMessage(), details));
        }

        // 서비스 계층일 경우 내부 오류로 판단
        return ResponseEntity
                .status(ErrorType.COMMON_SERVER_ERROR.getCode())
                .body(new ErrorResponse(ErrorType.COMMON_SERVER_ERROR.getMessage(), details));
    }

    // ModelAttribute, RequestParam 의 유효성 검사 예외를 처리합니다.
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception) {
        return ResponseEntity
                .status(ErrorType.CONSTRAINT_VIOLATION.getCode())
                .body(new ErrorResponse(ErrorType.CONSTRAINT_VIOLATION.getMessage()));
    }

    // ModelAttribute, RequestParam 의 유효성 검사 예외를 처리합니다.
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception) {
        return ApiResponseHelper.error(ErrorType.CONSTRAINT_VIOLATION.getCode(),
                new ErrorResponse(ErrorType.CONSTRAINT_VIOLATION.getMessage()));
    }

    // RequestParam, PathVariable 의 타입 변환 예외를 처리합니다.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        Detail detail = new Detail(
                exception.getName(),
                exception.getValue(),
                exception.getRequiredType().getSimpleName() + " 타입으로 변환할 수 없습니다.");

        return ResponseEntity
                .status(ErrorType.ARGUMENT_TYPE_MISMATCH.getCode())
                .body(new ErrorResponse(ErrorType.ARGUMENT_TYPE_MISMATCH.getMessage(),
                        List.of(detail)));
    }

    // RequestParam 의 누락 예외를 처리합니다.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        Detail detail = new Detail(
                exception.getParameterName(),
                exception.getParameterType() + " 타입의 요청 파라미터가 누락되었습니다.");

        return ResponseEntity
                .status(ErrorType.MISSING_REQUEST_PARAMETER.getCode())
                .body(new ErrorResponse(ErrorType.MISSING_REQUEST_PARAMETER.getMessage(),
                        List.of(detail)));
    }

    // PathVariable 의 누락 예외를 처리합니다.
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariableException(
            MissingPathVariableException exception) {
        Detail detail = new Detail(
                exception.getVariableName(),
                "해당 경로 파라미터가 누락되었습니다.");

        return ResponseEntity
                .status(ErrorType.MISSING_PATH_VARIABLE.getCode())
                .body(new ErrorResponse(ErrorType.MISSING_PATH_VARIABLE.getMessage(),
                        List.of(detail)));
    }

    // RequestBody 의 파싱 예외를 처리합니다.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        return ResponseEntity
                .status(ErrorType.JSON_NOT_READABLE.getCode())
                .body(new ErrorResponse(ErrorType.JSON_NOT_READABLE.getMessage()));
    }

    // HTTP 메소드의 지원 예외를 처리합니다.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(ErrorType.METHOD_NOT_ALLOWED.getCode())
                .body(new ErrorResponse(ErrorType.METHOD_NOT_ALLOWED.getMessage()));
    }
}