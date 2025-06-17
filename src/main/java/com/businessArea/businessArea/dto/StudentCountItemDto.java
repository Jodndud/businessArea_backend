package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// 개별 학교의 학생 수 정보를 나타내는 DTO
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentCountItemDto {

    @JsonProperty("SCHUL_CODE")
    private String schulCode;

    @JsonProperty("COL_FGR_SUM")
    private String colFgrSum; // "610(4)" 와 같은 문자열이므로 String으로 받음
}