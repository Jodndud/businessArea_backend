package com.businessArea.businessArea.repository; // 패키지 위치

import com.businessArea.businessArea.domain.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    // Sigungu 엔티티에 대한 DB 작업을 위한 인터페이스
}