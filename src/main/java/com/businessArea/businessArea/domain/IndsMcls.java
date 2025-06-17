package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inds_mcls")
public class IndsMcls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "md_id")
    private Long id;

    @Column(name = "inds_mcls_cd", nullable = false, unique = true)
    private String indsMclsCd; // 중분류코드

    @Column(name = "inds_mcls_nm", nullable = false)
    private String indsMclsNm; // 중분류명

    // IndsLcls(대분류)와의 다대일(N:1) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lg_id") // ERD의 L_id를 참조
    private IndsLcls indsLcls;

    public IndsMcls(String indsMclsCd, String indsMclsNm, IndsLcls indsLcls) {
        this.indsMclsCd = indsMclsCd;
        this.indsMclsNm = indsMclsNm;
        this.indsLcls = indsLcls;
    }
}