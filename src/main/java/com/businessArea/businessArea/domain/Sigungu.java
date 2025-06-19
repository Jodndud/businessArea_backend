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
@Table(name = "sigungu")
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sigungu_id")
    private Long sigunguId;

    @Column(name = "cd", unique = true, nullable = false)
    private String cd; // 시군구 코드

    @Column(name = "addr_name", nullable = false)
    private String addrName; // 시군구명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private Sido sido;

    @OneToMany(mappedBy = "sigungu", cascade = CascadeType.ALL)
    private Set<Adong> adongs = new HashSet<>();

    public Sigungu(String cd, String addrName, Sido sido) {
        this.cd = cd;
        this.addrName = addrName;
        this.sido = sido;
    }
}