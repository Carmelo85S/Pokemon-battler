package org.java26.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.java26.config.EntityManagerUtil;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public class PokemonRepositoryImpl implements PokemonRepository {

    @Override
    public Optional<Pokemon> findById(int id) {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            Pokemon pokemon = em.find(Pokemon.class, id);
            return Optional.ofNullable(pokemon);
        }
    }

    @Override
    public List<Pokemon> findAll() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.createQuery("SELECT DISTINCT p FROM Pokemon p LEFT JOIN FETCH p.attacks", Pokemon.class)
                    .getResultList();
        }
    }

    @Override
    public void save(Pokemon pokemon) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(pokemon);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            Pokemon pokemon = em.find(Pokemon.class, id);
            if(pokemon == null){
                System.err.println("  Pokemon not found.");
                return;
            }
                em.getTransaction().begin();
                em.remove(pokemon);
                em.getTransaction().commit();
        }
    }
}
