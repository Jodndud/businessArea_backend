package com.businessArea.businessArea.service;

import com.businessArea.businessArea.dto.kostat.KostatAuthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KostatAuthService {

    private final WebClient webClient;

    // application.properties에 키 값들이 설정되어 있어야 합니다.
    @Value("${sgis.consumer-key}")
    private String consumerKey;
    @Value("${sgis.consumer-secret}")
    private String consumerSecret;

    private static final String AUTH_URL = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json";

    /**
     * 통계청 API 인증 토큰을 발급받습니다.
     * @return Access Token 문자열
     */
    public String getAccessToken() {
        KostatAuthTokenDto response = webClient.get()
                .uri(AUTH_URL, uriBuilder -> uriBuilder
                        .queryParam("consumer_key", consumerKey)
                        .queryParam("consumer_secret", consumerSecret)
                        .build())
                .retrieve()
                .bodyToMono(KostatAuthTokenDto.class)
                .block(); // 동기 방식으로 결과를 기다립니다.

        if (response != null && response.getResult() != null) {
            return response.getResult().getAccessToken();
        }
        throw new RuntimeException("통계청 API 인증 토큰을 발급받지 못했습니다.");
    }
}