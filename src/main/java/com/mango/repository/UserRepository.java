package com.mango.repository;

import com.mango.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository  extends JpaRepository<User,Long> {
//    Optional<User> findByKakaoIdAndPassword(String kakaoId, String password);
    Optional<User> findByKakaoId(Long kakaoId);
}
