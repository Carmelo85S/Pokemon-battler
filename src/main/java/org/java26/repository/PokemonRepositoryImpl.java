package org.java26.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.java26.config.EntityManagerUtil;
import org.java26.exceptions.InvalidPokemonException;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public class PokemonRepositoryImpl implements PokemonRepository {

    @Override
    public Optional<Pokemon> findById(Long id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT p FROM Pokemon p " +
                                    "LEFT JOIN FETCH p.attacks " +
                                    "WHERE p.id = :id",
                            Pokemon.class
                    )
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst();
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
    public List<Pokemon> findByName(String name) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.createQuery("SELECT DISTINCT p FROM Pokemon p " +
                            "LEFT JOIN FETCH p.attacks " +
                            "WHERE LOWER(p.name) = LOWER(:name)", Pokemon.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Pokemon savedPokemon = em.merge(pokemon);
            tx.commit();
            return savedPokemon;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Pokemon pokemon = em.find(Pokemon.class, id);
            if (pokemon == null) {
                throw new InvalidPokemonException("Pokemon not found.");
            }
            em.remove(pokemon);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteByName(String name) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Pokemon> pokemons = em.createQuery(
                            "SELECT DISTINCT p FROM Pokemon p " +
                                    "LEFT JOIN FETCH p.attacks " +
                                    "WHERE LOWER(p.name) = LOWER(:name)",
                            Pokemon.class
                    )
                    .setParameter("name", name)
                    .getResultList();

            if (pokemons.isEmpty()) {
                throw new InvalidPokemonException("Pokemon not found.");
            }

            for (Pokemon pokemon : pokemons) {
                em.remove(pokemon);
            }

            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.createQuery("DELETE FROM Attack").executeUpdate();
            em.createQuery("DELETE FROM Pokemon").executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateLevel(Pokemon pokemon) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(pokemon);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
