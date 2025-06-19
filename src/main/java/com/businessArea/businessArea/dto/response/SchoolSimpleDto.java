package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.domain.School;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 행정동 상세 정보에 포함될 학교 목록의 개별 항목 DTO
 */
@Getter
public class SchoolSimpleDto {
    @JsonProperty("학교명")
    private String schoolName;

    @JsonProperty("학생수")
    private Integer studentCount;

    @JsonProperty("학교급")
    private String schoolType;

    public SchoolSimpleDto(School school) {
        this.schoolName = school.getSchulNm();
        this.studentCount = school.getColFgrSum();
        this.schoolType = school.getSchulCrseScValueNm();
    }
}