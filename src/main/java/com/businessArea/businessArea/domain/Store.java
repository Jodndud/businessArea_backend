package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특정 행정동의 특정 소분류 업종에 대한 통계 정보 (기업체수, 종사자수 등)를 저장하는 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inds_lcls_cd")
    private String indsLclsCd; // 상권업종대분류코드

    @Column(name = "inds_lcls_nm")
    private String indsLclsNm; // 상권업종대분류명

    @Column(name = "inds_scls_cd")
    private String indsSclsCd; // 상권업종소분류코드

    @Column(name = "inds_scls_nm")
    private String indsSclsNm; // 상권업종소분류명

    @Column(name = "corp_cnt")
    private Integer corpCnt;   // 기업체수

    @Column(name = "tot_worker")
    private Integer totWorker; // 종사자수

    /**
     * Adong(행정동)과의 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adong_id")
    private Adong adong;

    /**
     * IndsScls(업종소분류)와의 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sm_id")
    private IndsScls indsScls;


    // 서비스 레이어에서 객체 생성을 위한 생성자
    public Store(String indsLclsCd, String indsLclsNm, String indsSclsCd, String indsSclsNm,
                 Integer corpCnt, Integer totWorker, Adong adong, IndsScls indsScls) {
        this.indsLclsCd = indsLclsCd;
        this.indsLclsNm = indsLclsNm;
        this.indsSclsCd = indsSclsCd;
        this.indsSclsNm = indsSclsNm;
        this.corpCnt = corpCnt;
        this.totWorker = totWorker;
        this.adong = adong;
        this.indsScls = indsScls;
    }
}