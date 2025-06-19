package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.Sido;
import com.businessArea.businessArea.domain.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    // ✅ `StoreService`가 호출하는 메서드 추가
    Optional<Sigungu> findByAddrNameAndSido(String addrName, Sido sido);
}