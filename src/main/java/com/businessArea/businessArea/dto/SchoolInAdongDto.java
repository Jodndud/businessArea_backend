// 📁 dto/SchoolInAdongDto.java
package com.businessArea.businessArea.dto;

import com.businessArea.businessArea.domain.School;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SchoolInAdongDto {

    @JsonProperty("학교명")
    private String schoolName;
    @JsonProperty("학생수")
    private Integer studentCount;
    @JsonProperty("학교급")
    private String schoolType;

    public SchoolInAdongDto(School school) {
        this.schoolName = school.getSchulNm();
        this.studentCount = school.getColFgrSum();
        this.schoolType = school.getSchulCrseScValueNm();
    }
}