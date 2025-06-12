package com.businessArea.businessArea.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// DB에 'users'라는 이름의 테이블을 생성합니다. 'user'는 MySQL의 예약어일 수 있어 users를 사용합니다.
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // null을 허용하지 않고, 값이 중복될 수 없음
    private String username; // 사용자 로그인 ID

    @Column(nullable = false)
    private String password; // 사용자 비밀번호 (암호화하여 저장 예정)

    @Column(nullable = false)
    private String role; // 사용자 권한 (예: "ROLE_USER", "ROLE_ADMIN")

    // --- Constructors, Getters ---

    protected User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
