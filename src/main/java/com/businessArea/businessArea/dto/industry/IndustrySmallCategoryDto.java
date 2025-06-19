package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * industry_codes.json 파일의 소분류 객체 하나를 나타내는 DTO
 */
@Getter
@Setter
public class IndustrySmallCategoryDto {

    @JsonProperty("inds_scls_cd")
    private String smallCategoryCode;

    @JsonProperty("inds_scls_nm")
    private String smallCategoryName;
}