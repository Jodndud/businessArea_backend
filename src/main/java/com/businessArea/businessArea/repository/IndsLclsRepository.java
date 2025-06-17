package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.IndsLcls;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndsLclsRepository extends JpaRepository<IndsLcls, Long> {
    // 코드로 대분류 정보를 조회할 때 사용될 수 있습니다.
    Optional<IndsLcls> findByIndsLclsCd(String indsLclsCd);
}