package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id") // DB 테이블의 컬럼명을 명확히 지정
    private Long id;

    @Column(name = "ctprvn_cd", unique = true) // 시도 코드는 고유해야 하므로 unique = true 추가
    private String ctprvnCd;

    @Column(name = "ctprvn_nm") // 시도 이름
    private String ctprvnNm;

    // '일대다' 관계 설정: 하나의 시도(Sido)는 여러개의 시군구(Sigungu)를 가짐
    @OneToMany(mappedBy = "sido", cascade = CascadeType.ALL)
    private List<Sigungu> sigungus = new ArrayList<>();

    // --- Constructors, Getters ---

    protected Sido() {
    }

    public Sido(String ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }

    public Long getId() {
        return id;
    }

    public String getCtprvnCd() {
        return ctprvnCd;
    }

    public String getCtprvnNm() {
        return ctprvnNm;
    }

    public List<Sigungu> getSigungus() {
        return sigungus;
    }
}