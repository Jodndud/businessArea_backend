package com.businessArea.businessArea.dto.theme;

import com.businessArea.businessArea.domain.IndsLcls;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 대분류 정보를 담는 DTO
 */
@Getter
public class ThemeLclsDto {

    @JsonProperty("대분류코드")
    private String lclsCode;

    @JsonProperty("대분류명")
    private String lclsName;

    @JsonProperty("소분류_목록")
    private List<ThemeSclsDto> sclsList;

    public ThemeLclsDto(IndsLcls lcls) {
        this.lclsCode = lcls.getIndsLclsCd();
        this.lclsName = lcls.getIndsLclsNm();
        // 대분류 엔티티에 포함된 소분류 엔티티 리스트를 DTO 리스트로 변환
        this.sclsList = lcls.getIndsSclsList().stream()
                .map(ThemeSclsDto::new)
                .collect(Collectors.toList());
    }
}