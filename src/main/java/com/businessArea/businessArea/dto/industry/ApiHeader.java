package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * API 응답의 헤더 정보를 담습니다. (성공/실패 코드 및 메시지)
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiHeader {
    private String resultCode;
    private String resultMsg;
}