package com.businessArea.businessArea.dto;

/**
 * 회원가입 요청을 받을 때 사용하는 DTO (Data Transfer Object)
 */
public class SignupRequestDto {

    private String username;
    private String password;

    // --- Getters and Setters ---
    // 프론트엔드에서 보낸 JSON 데이터를 자바 객체로 변환하기 위해 필요합니다.
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}