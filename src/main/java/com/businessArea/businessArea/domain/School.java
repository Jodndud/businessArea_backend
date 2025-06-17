package com.businessArea.businessArea.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adong_id")
    private Adong adong;

    @Column(name = "ADRES_BRKDN")
    private String adresBrkdn;

    @Column(name = "SCHUL_NM")
    private String schulNm;

    @Column(name = "SCHUL_CRSE_SC_VALUE_NM")
    private String schulCrseScValueNm;

    @Column(name = "SCHUL_CODE")
    private String schulCode;

    @Column(name = "COL_FGR_SUM")
    private Integer colFgrSum;

    public School(Adong adong, String adresBrkdn, String schulNm, String schulCrseScValueNm, String schulCode) {
        this.adong = adong;
        this.adresBrkdn = adresBrkdn;
        this.schulNm = schulNm;
        this.schulCrseScValueNm = schulCrseScValueNm;
        this.schulCode = schulCode;
        this.colFgrSum = null;
    }
}