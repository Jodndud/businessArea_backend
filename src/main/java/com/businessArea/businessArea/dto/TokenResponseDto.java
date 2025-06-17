package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 모르는 필드는 무시
public class TokenResponseDto {
    private TokenResultDto result;
}