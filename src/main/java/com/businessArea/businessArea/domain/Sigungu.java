package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sigungu_id")
    private Long id;

    @Column(name = "sigungu_cd", unique = true) // 시군구 코드는 고유해야 함
    private String sigunguCd;

    @Column(name = "sigungu_nm")
    private String sigunguNm;

    // '다대일' 관계 설정: 여러개의 시군구(Sigungu)는 하나의 시도(Sido)에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id") // DB에 생성될 외래 키(Foreign Key) 컬럼 이름
    private Sido sido;

    // '일대다' 관계 설정: 하나의 시군구(Sigungu)는 여러개의 행정동(Adong)을 가짐
    @OneToMany(mappedBy = "sigungu", cascade = CascadeType.ALL)
    private List<Adong> adongs = new ArrayList<>();

    // --- Constructors, Getters ---

    protected Sigungu() {
    }

    public Sigungu(String sigunguCd, String sigunguNm, Sido sido) {
        this.sigunguCd = sigunguCd;
        this.sigunguNm = sigunguNm;
        this.sido = sido;
    }

    public Long getId() {
        return id;
    }

    public String getSigunguCd() {
        return sigunguCd;
    }

    public String getSigunguNm() {
        return sigunguNm;
    }

    public Sido getSido() {
        return sido;
    }

    public List<Adong> getAdongs() {
        return adongs;
    }

}