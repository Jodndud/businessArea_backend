package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 상권 소분류 API의 개별 항목(item) 데이터를 담는 DTO 입니다.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SclsItemDto {
    private String indsLclsCd;
    private String indsLclsNm;
    private String indsMclsCd;
    private String indsMclsNm;
    private String indsSclsCd; // 소분류 코드
    private String indsSclsNm; // 소분류 명
}