package com.brainstrom.meokjang.food.repository;

import com.brainstrom.meokjang.food.domain.FoodInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FoodInfoRepository {

    @PersistenceContext
    private EntityManager em;

    public FoodInfo save(FoodInfo foodInfo) {
        em.persist(foodInfo);
        return foodInfo;
    }

    public Optional<FoodInfo> findById(Long id) {
        return Optional.ofNullable(em.find(FoodInfo.class, id));
    }

    public List<FoodInfo> findByName(String name) {
        return em.createQuery("select f from FoodInfo f where f.InfoName = :name", FoodInfo.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<FoodInfo> findAll() {
        return em.createQuery("select f from FoodInfo f", FoodInfo.class)
                .getResultList();
    }
}
