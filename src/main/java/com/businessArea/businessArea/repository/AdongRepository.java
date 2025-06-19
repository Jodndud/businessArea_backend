package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdongRepository extends JpaRepository<Adong, Long> {

    // ✅ `StoreService`가 호출하는 메서드 추가
    Optional<Adong> findByAddrNameAndSigungu(String addrName, Sigungu sigungu);

    /**
     * 행정동 코드로 Adong을 조회할 때, 연관된 모든 엔티티(Sigungu, Sido, Boundary, Schools)를
     * Fetch Join을 이용해 한 번의 쿼리로 함께 가져옵니다. (N+1 문제 해결)
     * @param adongCd 행정동 코드
     * @return Adong 엔티티 (모든 연관 데이터 포함)
     */
    @Query("SELECT a FROM Adong a " +
            "LEFT JOIN FETCH a.sigungu s " +
            "LEFT JOIN FETCH s.sido sd " +
            "LEFT JOIN FETCH a.boundary b " +
            "LEFT JOIN FETCH a.schools sc " +
            "WHERE a.cd = :adongCd")
    Optional<Adong> findByCdWithDetails(@Param("adongCd") String adongCd);
}