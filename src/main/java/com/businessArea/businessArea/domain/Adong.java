package com.businessArea.businessArea.domain;

import jakarta.persistence.*;

@Entity
public class Adong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adong_id")
    private Long id;

    @Column(name = "adong_cd", unique = true)
    private String adongCd;

    @Column(name = "adong_nm")
    private String adongNm;

    // '다대일' 관계 설정: 여러개의 행정동(Adong)은 하나의 시군구(Sigungu)에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigungu_id") // DB에 생성될 외래 키(Foreign Key) 컬럼 이름
    private Sigungu sigungu;

    // --- Constructors, Getters ---

    protected Adong() {
    }

    public Adong(String adongCd, String adongNm, Sigungu sigungu) {
        this.adongCd = adongCd;
        this.adongNm = adongNm;
        this.sigungu = sigungu;
    }

    public Long getId() {
        return id;
    }

    public String getAdongCd() {
        return adongCd;
    }

    public String getAdongNm() {
        return adongNm;
    }

    public Sigungu getSigungu() {
        return sigungu;
    }
}