package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inds_scls")
public class IndsScls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sm_id")
    private Long id;

    // 다른 대분류에 동일한 소분류 코드가 존재할 수 있으므로 unique 제약조건을 제거합니다.
    @Column(name = "inds_scls_cd", nullable = false)
    private String indsSclsCd; // 소분류코드

    @Column(name = "inds_scls_nm", nullable = false)
    private String indsSclsNm; // 소분류명

    // 대분류(IndsLcls)와의 다대일(N:1) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lg_id") // DB의 외래 키 컬럼명
    private IndsLcls indsLcls;

    public IndsScls(String indsSclsCd, String indsSclsNm, IndsLcls indsLcls) {
        this.indsSclsCd = indsSclsCd;
        this.indsSclsNm = indsSclsNm;
        this.indsLcls = indsLcls;
    }
}