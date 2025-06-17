package com.businessArea.businessArea.repository; // 패키지 위치

import com.businessArea.businessArea.domain.Adong;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdongRepository extends JpaRepository<Adong, Long> {
    // 모든 Adong 데이터를 가져올 때, 연관된 Sigungu와 Sido를 함께 fetch join 하여 가져옴
    @Query("SELECT a FROM Adong a JOIN FETCH a.sigungu s JOIN FETCH s.sido")
    List<Adong> findAllWithSigunguAndSido();
}