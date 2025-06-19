package com.businessArea.businessArea.dto.theme;

import com.businessArea.businessArea.domain.IndsScls;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 소분류 정보를 담는 DTO
 */
@Getter
public class ThemeSclsDto {

    @JsonProperty("소분류코드")
    private String sclsCode;

    @JsonProperty("소분류명")
    private String sclsName;

    public ThemeSclsDto(IndsScls scls) {
        this.sclsCode = scls.getIndsSclsCd();
        this.sclsName = scls.getIndsSclsNm();
    }
}