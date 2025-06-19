package com.businessArea.businessArea.repository; // 패키지 위치

import com.businessArea.businessArea.domain.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SidoRepository extends JpaRepository<Sido, Long> {
    // Sido 엔티티에 대한 DB 작업을 위한 인터페이스
    // JpaRepository<[엔티티 클래스명], [ID 필드 타입]>
    Optional<Sido> findByAddrName(String addrName);
    /**
     * 프론트엔드의 드롭다운 메뉴를 채우기 위해,
     * 모든 시도-시군구-행정동 정보를 한 번의 쿼리로 가져옵니다.
     */
    @Query("SELECT DISTINCT s FROM Sido s LEFT JOIN FETCH s.sigungus sg LEFT JOIN FETCH sg.adongs")
    List<Sido> findAllWithSigunguAndAdong();
}