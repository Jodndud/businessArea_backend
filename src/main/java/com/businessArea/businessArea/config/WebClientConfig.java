package com.businessArea.businessArea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // ✅ API 응답을 처리할 메모리 버퍼의 크기를 설정합니다.
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB로 상향
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies) // ✅ 생성된 전략을 WebClient에 적용
                .build();
    }
}