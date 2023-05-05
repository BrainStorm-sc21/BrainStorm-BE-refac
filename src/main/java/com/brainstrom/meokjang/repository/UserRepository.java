package com.brainstrom.meokjang.repository;

import com.brainstrom.meokjang.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public Optional<User> findByName(String name) {
        List<User> result = em.createQuery("select u from User u where u.userName = :name", User.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
}
