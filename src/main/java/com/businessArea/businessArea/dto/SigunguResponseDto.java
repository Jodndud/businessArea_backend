package com.businessArea.businessArea.dto;

public class SigunguResponseDto {
    private String sigunguCd; // 시군구 코드
    private String sigunguNm; // 시군구명

    // 생성자, Getter
    public SigunguResponseDto(String sigunguCd, String sigunguNm) {
        this.sigunguCd = sigunguCd;
        this.sigunguNm = sigunguNm;
    }

    public String getSigunguCd() { return sigunguCd; }
    public String getSigunguNm() { return sigunguNm; }
}