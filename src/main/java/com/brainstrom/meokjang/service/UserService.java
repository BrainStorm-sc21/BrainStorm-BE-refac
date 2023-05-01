package com.brainstrom.meokjang.service;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long join(User user) {
        try {
            validateDuplicateUser(user);
            userRepository.save(user);
            return user.getUserId();
        } finally {

        }
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByName(user.getUserName())
                        .ifPresent(m -> {
                            throw new IllegalStateException("이미 존재하는 회원입니다.");
                        });
    }
}
