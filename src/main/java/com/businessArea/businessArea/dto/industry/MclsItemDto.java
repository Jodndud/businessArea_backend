package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 상권 중분류 API의 개별 항목(item) 데이터를 담는 DTO 입니다.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MclsItemDto {
    private String indsLclsCd;
    private String indsLclsNm;
    private String indsMclsCd; // 중분류 코드
    private String indsMclsNm; // 중분류 명
}