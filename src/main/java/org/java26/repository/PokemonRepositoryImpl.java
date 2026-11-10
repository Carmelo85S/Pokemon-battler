package org.java26.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.java26.config.EntityManagerUtil;
import org.java26.models.Pokedex;
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
    public List<Pokemon> findByName(String name) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.createQuery("SELECT p from Pokemon p where p.name = :name", Pokemon.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }

    @Override
    public void save(Pokemon pokemon) {
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

    @Override
    public void delete(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Pokemon pokemon = em.find(Pokemon.class, id);
            if (pokemon == null) {
                tx.rollback();
                System.err.println("  Pokemon not found.");
                return;
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

            int deleted = em.createQuery(
                            "DELETE FROM Pokemon p WHERE LOWER(p.name) = LOWER(:name)"
                    )
                    .setParameter("name", name)
                    .executeUpdate();

            tx.commit();

            if (deleted == 0) {
                System.err.println("  Pokemon not found.");
            }

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
