package com.businessArea.businessArea.dto;

public class SidoResponseDto {
    private String ctprvnCd; // 시도 코드
    private String ctprvnNm; // 시도명

    // 생성자, Getter
    public SidoResponseDto(String ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }

    public String getCtprvnCd() { return ctprvnCd; }
    public String getCtprvnNm() { return ctprvnNm; }
}