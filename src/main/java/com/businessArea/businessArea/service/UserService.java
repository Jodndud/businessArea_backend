package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.User;
import com.businessArea.businessArea.dto.SignupRequestDto;
import com.businessArea.businessArea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자를 통해 UserRepository와 PasswordEncoder를 주입받습니다.
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 로직을 처리하는 메소드
     * @param requestDto 회원가입 정보를 담은 DTO
     */
    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();

        // 1. 아이디 중복 확인
        if (userRepository.existsByUsername(username)) {
            // 이미 존재하는 아이디일 경우, 예외를 발생시킵니다.
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. 사용자 권한 설정 (기본값: ROLE_USER)
        String role = "ROLE_USER";

        // 4. User 엔티티 생성 및 저장
        User user = new User(username, encodedPassword, role);
        userRepository.save(user);
    }
}