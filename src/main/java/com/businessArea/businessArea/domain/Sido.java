package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor // JPA는 기본 생성자가 필수입니다.
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
    private List<Sigungu> sigungus = new ArrayList<>();

    // AddressService에서 사용할 생성자
    public Sido(String cd, String addrName) {
        this.cd = cd;
        this.addrName = addrName;
    }
}