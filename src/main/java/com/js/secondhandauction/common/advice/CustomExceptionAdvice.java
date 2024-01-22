package com.js.secondhandauction.common.advice;

import com.js.secondhandauction.common.exception.CustomException;
import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.common.response.ApiResponse;
import com.js.secondhandauction.common.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<ErrorResult>> handleCustomException(CustomException e) {
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        log.debug("custom exception!! error msg={}", errorResult);
        return ResponseEntity.badRequest().body(ApiResponse.fail(errorResult));
    }
}
