package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.IndsLcls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface IndsLclsRepository extends JpaRepository<IndsLcls, Long> {

    Optional<IndsLcls> findByIndsLclsCd(String indsLclsCd);

    /**
     * ✅ [추가] 모든 대분류를 조회할 때, 연관된 소분류 목록까지 한번에 가져오는 쿼리
     */
    @Query("SELECT DISTINCT l FROM IndsLcls l LEFT JOIN FETCH l.indsSclsList")
    List<IndsLcls> findAllWithScls();
}