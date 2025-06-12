package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.dto.LoginRequestDto; // 추가된 import
import com.businessArea.businessArea.dto.SignupRequestDto;
import com.businessArea.businessArea.service.UserService;
import jakarta.servlet.http.HttpServletRequest; // 추가된 import
import jakarta.servlet.http.HttpServletResponse; // 추가된 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // 추가된 import
import org.springframework.security.core.context.SecurityContextHolder; // 추가된 import
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler; // 추가된 import
import org.springframework.web.bind.annotation.GetMapping; // 추가된 import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        try {
            userService.signup(requestDto);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 로그인 API
     * 실제 인증 처리는 JwtAuthenticationFilter에서 수행됩니다.
     * 이 엔드포인트는 클라이언트가 로그인 요청을 보낼 주소 역할을 합니다.
     * 필터에서 인증 실패 시 에러를 반환하므로, 이 메소드는 호출되지 않습니다.
     * 인증 성공 시 필터에서 직접 응답을 처리하므로, 이 메소드 또한 호출되지 않습니다.
     * (이 엔드포인트가 존재해야 Spring Security가 요청을 가로챌 수 있습니다)
     */
    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto) {
        // 실제 로직은 필터에 있으므로 이 메소드의 내용은 비워둡니다.
    }

    /**
     * 로그아웃 API
     */
    @PostMapping("/logout") // @GetMapping에서 @PostMapping으로 변경
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 사용자 정보를 가져옵니다.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Spring Security의 로그아웃 핸들러를 사용하여 세션을 무효화하고 컨텍스트를 클리어합니다.
            // (JWT 방식에서는 주로 클라이언트 측에서 토큰을 삭제하는 것이 핵심입니다)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
