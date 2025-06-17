package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * API 응답의 가장 바깥쪽 구조를 감싸는 제네릭 클래스입니다.
 * @param <T> item의 타입 (예: LclsItemDto, MclsItemDto)
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private ApiHeader header;
    private ApiBody<T> body;
}