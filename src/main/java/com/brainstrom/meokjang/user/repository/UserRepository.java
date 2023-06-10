package com.brainstrom.meokjang.user.repository;

import com.brainstrom.meokjang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findBySnsTypeAndSnsKey(String snsType, String snsKey);

    @Modifying
    @Query("update User u set u.userName = :userName where u.userId = :userId")
    int updateUserById(Long userId, String userName);

    @Modifying
    @Query("update User u set u.reliability = :reliability where u.userId = :userId")
    int updateReliabilityById(Long userId, Float reliability);
}
