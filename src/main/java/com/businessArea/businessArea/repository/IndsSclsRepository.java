package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.IndsScls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 상권 소분류(IndsScls) 엔티티를 위한 Repository
 */
@Repository
public interface IndsSclsRepository extends JpaRepository<IndsScls, Long> {
    // ✅ themeCode로 소분류를 찾기 위한 메서드 추가
    Optional<IndsScls> findByIndsSclsCd(String indsSclsCd);
}