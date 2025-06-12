package com.businessArea.businessArea.dto;

public class AdongResponseDto {
    private String adongCd; // 행정동 코드
    private String adongNm; // 행정동명

    // 생성자, Getter
    public AdongResponseDto(String adongCd, String adongNm) {
        this.adongCd = adongCd;
        this.adongNm = adongNm;
    }

    public String getAdongCd() { return adongCd; }
    public String getAdongNm() { return adongNm; }
}