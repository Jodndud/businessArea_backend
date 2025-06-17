package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // JPA는 기본 생성자가 필수입니다.
@Table(name = "adong")
public class Adong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adong_id")
    private Long adongId;

    @Column(name = "cd", unique = true, nullable = false)
    private String cd; // 행정동 코드

    @Column(name = "addr_name", nullable = false)
    private String addrName; // 행정동명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigungu_id")
    private Sigungu sigungu;

    // AddressService에서 사용할 생성자
    public Adong(String cd, String addrName, Sigungu sigungu) {
        this.cd = cd;
        this.addrName = addrName;
        this.sigungu = sigungu;
    }
}