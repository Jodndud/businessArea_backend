package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inds_lcls")
public class IndsLcls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lg_id")
    private Long id;

    @Column(name = "inds_lcls_cd", nullable = false, unique = true)
    private String indsLclsCd; // 대분류코드

    @Column(name = "inds_lcls_nm", nullable = false)
    private String indsLclsNm; // 대분류명

    // 소분류(IndsScls)와의 일대다(1:N) 관계 설정
    @OneToMany(mappedBy = "indsLcls", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndsScls> indsSclsList = new ArrayList<>();

    public IndsLcls(String indsLclsCd, String indsLclsNm) {
        this.indsLclsCd = indsLclsCd;
        this.indsLclsNm = indsLclsNm;
    }
}