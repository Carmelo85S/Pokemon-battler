package org.java26.repository;

import jakarta.persistence.EntityManager;
import org.java26.config.EntityManagerUtil;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public class PokemonRepositoryImpl implements PokemonRepository {

    @Override
    public Optional<Pokemon> findById(Long id) {

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
            Pokemon savedPokemon = em.merge(pokemon);
            em.getTransaction().commit();
            System.out.println("ID: " + savedPokemon.getId());
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            em.getTransaction().begin();
            Pokemon pokemon = em.find(Pokemon.class, id);
            if (pokemon == null) {
                em.getTransaction().rollback();

                System.err.println("  Pokemon not found.");
                return;
            }
            em.remove(pokemon);
            em.getTransaction().commit();
        }
    }

}
