package com.businessArea.businessArea.repository; // 패키지 위치

import com.businessArea.businessArea.domain.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoRepository extends JpaRepository<Sido, Long> {
    // Sido 엔티티에 대한 DB 작업을 위한 인터페이스
    // JpaRepository<[엔티티 클래스명], [ID 필드 타입]>
}