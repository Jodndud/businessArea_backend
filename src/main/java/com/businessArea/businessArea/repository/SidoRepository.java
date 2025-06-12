package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SidoRepository extends JpaRepository<Sido, Long> {
    Optional<Sido> findByCtprvnCd(String ctprvnCd);
}