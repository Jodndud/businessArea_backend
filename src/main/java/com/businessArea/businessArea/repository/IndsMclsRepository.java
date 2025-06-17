package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.IndsMcls;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndsMclsRepository extends JpaRepository<IndsMcls, Long> {
    // 코드로 중분류 정보를 조회할 때 사용될 수 있습니다.
    Optional<IndsMcls> findByIndsMclsCd(String indsMclsCd);
}