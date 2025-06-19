package com.businessArea.businessArea.dto.kostat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // API 응답이 비어있을 때를 대비해 기본 생성자 추가
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreStatsItemDto {

    @JsonProperty("corp_cnt")
    private String corpCnt; // 기업체수

    @JsonProperty("tot_worker")
    private String totWorker; // 종사자수

    @JsonProperty("sido_nm")
    private String sidoNm; // 시도명

    @JsonProperty("sgg_nm")
    private String sigunguNm; // 시군구명

    @JsonProperty("adm_nm")
    private String adongNm; // 행정동명

    @JsonProperty("theme_nm")
    private String themeNm; // 업종명
}