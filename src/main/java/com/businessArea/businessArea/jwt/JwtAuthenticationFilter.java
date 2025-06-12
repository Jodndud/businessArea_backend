package com.businessArea.businessArea.jwt;

import com.businessArea.businessArea.dto.LoginRequestDto;
import com.businessArea.businessArea.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        // 로그인 요청을 처리할 URL을 설정합니다.
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 본문(JSON)을 LoginRequestDto 객체로 변환합니다.
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // 인증을 시도합니다. Spring Security가 UserDetailsService를 호출하여 사용자를 확인합니다.
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 인증에 성공했을 때 실행됩니다.
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.createToken(username);

        // JWT를 응답 헤더에 추가합니다.
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write("로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 인증에 실패했을 때 실행됩니다.
        response.setStatus(401); // 401 Unauthorized
        response.getWriter().write("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}