package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.IndsScls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndsSclsRepository extends JpaRepository<IndsScls, Long> {
    // 추가적인 메서드 선언이 당장은 필요 없습니다.
}