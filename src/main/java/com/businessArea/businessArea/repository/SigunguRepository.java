package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.Sido;
import com.businessArea.businessArea.domain.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    Optional<Sigungu> findBySigunguCd(String sigunguCd);

    List<Sigungu> findAllBySido(Sido sido);
}