// 📁 domain/Adong.java
package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // ✅ School 과의 일대다(1:N) 관계 추가
    @OneToMany(mappedBy = "adong")
    private Set<School> schools = new HashSet<>();

    // ✅ Store 와의 일대다(1:N) 관계 추가
    @OneToMany(mappedBy = "adong")
    private Set<Store> stores = new HashSet<>();

    // ✅ Boundary 와의 일대일(1:1) 관계 추가
    @OneToOne(mappedBy = "adong", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Boundary boundary;

    public Adong(String cd, String addrName, Sigungu sigungu) {
        this.cd = cd;
        this.addrName = addrName;
        this.sigungu = sigungu;
    }
}