// 📁 domain/Boundary.java
package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boundary")
public class Boundary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boundary_id")
    private Long boundaryId;

    // GEOMETRY 타입은 대용량일 수 있으므로 @Lob 어노테이션 추가
    @Lob
    @Column(name = "coordinates", columnDefinition = "GEOMETRY")
    private Polygon coordinates;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adong_id")
    private Adong adong;

    public Boundary(Polygon coordinates, Adong adong) {
        this.coordinates = coordinates;
        this.adong = adong;
    }
}