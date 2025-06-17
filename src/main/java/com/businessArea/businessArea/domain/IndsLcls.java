package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inds_lcls") // 테이블명은 보통 snake_case를 사용합니다.
public class IndsLcls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lg_id")
    private Long id;

    @Column(name = "inds_lcls_cd", nullable = false, unique = true)
    private String indsLclsCd; // 대분류코드

    @Column(name = "inds_lcls_nm", nullable = false)
    private String indsLclsNm; // 대분류명

    public IndsLcls(String indsLclsCd, String indsLclsNm) {
        this.indsLclsCd = indsLclsCd;
        this.indsLclsNm = indsLclsNm;
    }
}