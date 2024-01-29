package com.js.secondhandauction.common.security.handler;

import com.js.secondhandauction.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(ErrorCode.ACCESS_DENIED.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(ErrorCode.ACCESS_DENIED.createErrorResponse());
    }
}
