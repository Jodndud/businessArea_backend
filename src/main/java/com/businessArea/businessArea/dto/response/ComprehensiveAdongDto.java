package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.Boundary;
import com.businessArea.businessArea.dto.kostat.StoreStatsItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 행정동 상세 정보 API의 최종 응답을 위한 DTO
 */
@Getter
public class ComprehensiveAdongDto {

    @JsonProperty("행정동_코드")
    private String adongCode;

    @JsonProperty("전체_주소")
    private String fullAddress;

    @JsonProperty("경계좌표")
    private String boundary;

    @JsonProperty("학교_목록")
    private List<SchoolSimpleDto> schools;

    @JsonProperty("업종_통계")
    private StoreStatsDto storeStats;

    /**
     * ✅ 생성자의 두 번째 파라미터 타입을 StoreStatsItemDto -> StoreStatsDto 로 수정합니다.
     * @param adong 서비스에서 조회한 Adong 엔티티
     * @param stats 서비스에서 미리 가공한 StoreStatsDto 객체
     */
    public ComprehensiveAdongDto(Adong adong, StoreStatsDto stats) {
        this.adongCode = adong.getCd();
        this.fullAddress = String.format("%s %s %s",
                adong.getSigungu().getSido().getAddrName(),
                adong.getSigungu().getAddrName(),
                adong.getAddrName());

        this.boundary = adong.getBoundary() != null ? adong.getBoundary().getCoordinates().toString() : null;

        this.schools = adong.getSchools().stream()
                .map(SchoolSimpleDto::new)
                .collect(Collectors.toList());

        // ✅ new StoreStatsDto(stats) 대신, 서비스에서 만들어 전달한 stats 객체를 그대로 할당합니다.
        this.storeStats = stats;
    }
}