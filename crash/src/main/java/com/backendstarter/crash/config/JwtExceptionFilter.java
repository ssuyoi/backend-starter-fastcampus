package com.backendstarter.crash.config;

import com.backendstarter.crash.model.error.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            // 문제없이 처리 되었다면
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // JwtException이 발생했을 경우 해당 response를 받아서 처리
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");

            var errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());

            // JSON 형태로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(responseJson);
        }

    }
}
