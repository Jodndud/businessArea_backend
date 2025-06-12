package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.Adong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.businessArea.businessArea.domain.Sigungu;
import java.util.List;

@Repository
public interface AdongRepository extends JpaRepository<Adong, Long> {
    Optional<Adong> findByAdongCd(String adongCd);

    List<Adong> findAllBySigungu(Sigungu sigungu);
}