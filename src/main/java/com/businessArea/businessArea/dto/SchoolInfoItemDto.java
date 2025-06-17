package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfoItemDto {

    @JsonProperty("ADRES_BRKDN")
    private String adresBrkdn;

    @JsonProperty("SCHUL_NM")
    private String schulNm;

    @JsonProperty("SCHUL_CODE")
    private String schulCode;

    @JsonProperty("SCHUL_CRSE_SC_VALUE_NM")
    private String schulCrseScValueNm;
}