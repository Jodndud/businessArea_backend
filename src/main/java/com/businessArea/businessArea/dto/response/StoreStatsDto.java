package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.dto.kostat.StoreStatsItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 행정동 상세 정보에 포함될 업종 통계 DTO
 */
@Getter
public class StoreStatsDto {

    @JsonProperty("기업체수")
    private String corpCnt;

    @JsonProperty("종사자수")
    private String totWorker;

    @JsonProperty("업종명")
    private String themeName;

    // ✅ 생성자를 수정하여, DB에서 찾은 업종명을 파라미터로 받습니다.
    public StoreStatsDto(StoreStatsItemDto item, String themeNameFromDb) {
        // DB에서 가져온 신뢰할 수 있는 업종명을 우선적으로 사용합니다.
        this.themeName = themeNameFromDb;

        if (item != null) {
            this.corpCnt = item.getCorpCnt();
            this.totWorker = item.getTotWorker();
            // 만약 DB에 업종명이 없는 경우, API의 업종명을 차선책으로 사용합니다.
            if (this.themeName == null || this.themeName.isBlank()) {
                this.themeName = item.getThemeNm();
            }
        }
    }
}