package com.brainstrom.meokjang.service;

import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public Long join(User user) {
        try {
            validateDuplicateUser(user);
            userRepository.save(user);
            return user.getUserId();
        } finally {

        }
    }

    public User login(String phoneNumber, String snsType, String snsKey) {
        try {
            User user;
            if ((user = userRepository.findByPhoneNumber(phoneNumber)) == null) {
                if ((user = userRepository.findBySns(snsType, snsKey)) == null) {
                    throw new IllegalStateException("존재하지 않는 회원입니다.");
                }
                else return user;
            }
            else return user;
        } finally {

        }
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByName(user.getUserName())
                        .ifPresent(m -> {
                            throw new IllegalStateException("이미 존재하는 회원입니다.");
                        });
    }

    public User findUser(long userId) {
        return userRepository.findById(userId);
    }

}
