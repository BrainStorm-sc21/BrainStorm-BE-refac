package com.brainstrom.meokjang.user.repository;

import com.brainstrom.meokjang.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
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

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        try {
            User result = em.createQuery("select u from User u where u.phoneNumber = :phoneNumber", User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findBySns(String snsType, String snsKey) {
        try {
            User result = em.createQuery("select u from User u where u.snsType = :snsType and u.snsKey = :snsKey", User.class)
                    .setParameter("snsType", snsType)
                    .setParameter("snsKey", snsKey)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void deleteAll() {
        em.createQuery("delete from User u")
                .executeUpdate();
    }
}
