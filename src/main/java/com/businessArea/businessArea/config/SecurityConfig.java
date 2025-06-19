package com.businessArea.businessArea.config;

import com.businessArea.businessArea.jwt.JwtAuthenticationFilter;
import com.businessArea.businessArea.jwt.JwtAuthorizationFilter;
import com.businessArea.businessArea.jwt.JwtUtil;
import com.businessArea.businessArea.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 세션 방식 대신 JWT 방식 사용 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        // 인증 관련 접근 허용
                        .requestMatchers("/api/signup/**", "/api/login").permitAll()
                        // 데이터 조회 접근 허용
                        .requestMatchers(HttpMethod.GET, "/api/sido", "/api/sigungu").permitAll()
                        // 데이터 저장 API (관리자용 - 지금은 편의상 허용, 나중에 권한 설정 필요)
                        .requestMatchers("/api/admin/addresses/update", "/api/admin/addresses/update-boundaries",
                                "/api/admin/schools/update", "/api/admin/schools/update/school-student-counts",
                                "/api/admin/industry/save-lcls", "/api/admin/industry/save-mcls", "/api/admin/industry/save-scls").permitAll()
                        // 데이터 요청 API(위치데이터 출력, 행정동정보 출력, 업종코드 출력)
                        .requestMatchers("/districts/all", "/adong-details/{adongCode}", "/theme_code/all").permitAll()
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
        );

        // 로그아웃 설정
        http.logout((logout) -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("로그아웃 되었습니다.");
                })
                .permitAll()
        );

        // 직접 만든 필터를 Security Filter Chain에 등록
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
