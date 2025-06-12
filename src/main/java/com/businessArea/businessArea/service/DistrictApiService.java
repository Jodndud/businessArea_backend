package com.businessArea.businessArea.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies; // 이 import를 추가하세요.
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DistrictApiService {

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String BASE_URL = "http://apis.data.go.kr/B553077/api/open/sdsc2";

    // 행정 경계 조회
    public String baroApi() {
        WebClient webClient = WebClient.create();
        return webClient.get()
                .uri(BASE_URL, uriBuilder -> uriBuilder
                        .path("/baroApi") // 'baroApi' 엔드포인트
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("resId", "dong") // 리소스 ID: 행정구역
                        .queryParam("catId", "cty")  // mega:시도, cty:시군구, admi:행정동, zone:법정동
                        .queryParam("ctprvnCd", "26") // 시도 코드: 서울특별시
                        .queryParam("type", "json") // 응답 형식: JSON
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // 행정동 단위 상가업소 조회
    public String storeZoneInAdmi() {
        // 1. 메모리 버퍼 사이즈를 늘리는 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB로 증설
                .build();

        // 2. WebClient를 생성할 때 위에서 만든 설정을 적용
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL) // Base URL을 여기서 설정
                .exchangeStrategies(exchangeStrategies) // 설정을 적용
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/storeZoneInAdmi")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "10")
                        .queryParam("divId", "ctprvnCd")
                        .queryParam("key", "11")
                        .queryParam("type", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // 상권정보 업종 소분류 코드
}