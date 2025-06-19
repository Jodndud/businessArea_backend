package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * industry_codes.json 파일의 대분류 객체 하나를 나타내는 DTO
 */
@Getter
@Setter
public class IndustryLargeCategoryDto {

    // JSON의 "inds_lcls_cd" 키를 이 필드에 매핑합니다.
    @JsonProperty("inds_lcls_cd")
    private String largeCategoryCode;

    @JsonProperty("inds_lcls_nm")
    private String largeCategoryName;

    // JSON의 "inds_scls" 키(소분류 목록)를 이 리스트 필드에 매핑합니다.
    @JsonProperty("inds_scls")
    private List<IndustrySmallCategoryDto> smallCategories;
}