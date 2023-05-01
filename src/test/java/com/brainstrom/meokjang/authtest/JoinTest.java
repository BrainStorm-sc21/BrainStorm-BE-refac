package com.brainstrom.meokjang.authtest;

import com.brainstrom.meokjang.AutoAppConfig;
import com.brainstrom.meokjang.domain.User;
import com.brainstrom.meokjang.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JoinTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

    @Test
    void join() {
        //given
        UserService userService = ac.getBean("userService", UserService.class);
        User user = new User("name", "010-1234-5678", "kakao", "경기도 수원시 어쩌궁", 36.5, 36.47777, 0);

        //when
        userService.join(user);
        User findUser = userService.findUser(user.getUserId());

        //then
        Assertions.assertThat(user).isEqualTo(findUser);
    }
}
