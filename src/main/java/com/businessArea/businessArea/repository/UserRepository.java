package com.businessArea.businessArea.repository;

import com.businessArea.businessArea.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * username으로 사용자를 찾는 기능
     * Spring Data JPA의 쿼리 메소드 기능 덕분에, 메소드 이름만으로 자동으로 SQL 쿼리가 생성됩니다.
     * @param username 사용자 로그인 ID
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);

    /**
     * username을 가진 사용자가 존재하는지 확인하는 기능
     * @param username 사용자 로그인 ID
     * @return boolean
     */
    boolean existsByUsername(String username);
}