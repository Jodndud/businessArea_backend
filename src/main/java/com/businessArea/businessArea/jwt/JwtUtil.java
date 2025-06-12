package com.businessArea.businessArea.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // 이 클래스를 스프링의 '부품'으로 등록합니다.
public class JwtUtil {

    // JWT를 만들 때 사용할 비밀 키. application.properties에 설정합니다.
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 토큰 생성
     * @param username 사용자 로그인 ID
     * @return 생성된 JWT 토큰
     */
    public String createToken(String username) {
        Date now = new Date();
        // 토큰 만료 시간: 1시간
        Date validity = new Date(now.getTime() + 3600 * 1000);

        return Jwts.builder()
                .setSubject(username) // 토큰의 주체(사용자 이름)
                .setIssuedAt(now)     // 토큰 발급 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(getSigningKey(), signatureAlgorithm) // 비밀 키로 서명
                .compact();
    }

    /**
     * 토큰에서 사용자 이름(username) 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우 (만료, 변조 등)
            return false;
        }
    }

    // 토큰에서 정보를 담고 있는 Claims 부분을 추출
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 비밀 키를 Key 객체로 변환
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}