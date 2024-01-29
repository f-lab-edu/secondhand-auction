package com.js.secondhandauction.common.advice;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<ErrorResult>> handleCustomException(CustomException e) {
        log.error("custom exception!! error msg={}", e.getMessage());

        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResult response = ErrorResult.of(errorCode);

        return new ResponseEntity<>(ApiResponse.fail(response), HttpStatus.valueOf(errorCode.getStatus()));
    }
}
