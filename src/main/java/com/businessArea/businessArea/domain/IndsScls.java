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

    @Column(name = "inds_scls_cd", nullable = false, unique = true)
    private String indsSclsCd; // 소분류코드

    @Column(name = "inds_scls_nm", nullable = false)
    private String indsSclsNm; // 소분류명

    // IndsMcls(중분류)와의 다대일(N:1) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "md_id") // ERD의 M-id를 참조
    private IndsMcls indsMcls;

    public IndsScls(String indsSclsCd, String indsSclsNm, IndsMcls indsMcls) {
        this.indsSclsCd = indsSclsCd;
        this.indsSclsNm = indsSclsNm;
        this.indsMcls = indsMcls;
    }
}