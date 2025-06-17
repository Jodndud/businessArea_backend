package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 모르는 필드는 무시
public class TokenResultDto {
    @JsonProperty("accessToken") // JSON의 'accessToken' 필드를 매핑
    private String accessToken;
}