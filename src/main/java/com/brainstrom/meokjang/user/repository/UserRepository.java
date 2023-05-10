package com.brainstrom.meokjang.user.repository;

import com.brainstrom.meokjang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findBySnsTypeAndSnsKey(String snsType, String snsKey);
}
