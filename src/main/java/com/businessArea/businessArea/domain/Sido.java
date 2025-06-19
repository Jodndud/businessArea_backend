package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 기본 생성자 사용을 막기 위해 PROTECTED로 변경
@Table(name = "sido")
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id")
    private Long sidoId;

    @Column(name = "cd", unique = true, nullable = false)
    private String cd; // 시도 코드

    @Column(name = "addr_name", nullable = false)
    private String addrName; // 시도명

    @OneToMany(mappedBy = "sido", cascade = CascadeType.ALL)
    private Set<Sigungu> sigungus = new HashSet<>();

    public Sido(String cd, String addrName) {
        this.cd = cd;
        this.addrName = addrName;
    }
}