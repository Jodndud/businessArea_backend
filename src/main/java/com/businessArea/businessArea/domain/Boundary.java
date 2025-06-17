package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boundary")
public class Boundary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boundary_id")
    private Long boundaryId;

    // 필드 타입을 Polygon으로 변경하고, DB 컬럼 타입을 GEOMETRY로 지정
    @Column(name = "coordinates", columnDefinition = "GEOMETRY")
    private Polygon coordinates;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adong_id")
    private Adong adong;

    // 생성자도 Polygon 타입을 받도록 수정
    public Boundary(Polygon coordinates, Adong adong) {
        this.coordinates = coordinates;
        this.adong = adong;
    }
}