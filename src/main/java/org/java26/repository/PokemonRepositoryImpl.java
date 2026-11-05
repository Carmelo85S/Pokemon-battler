package org.java26.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public class PokemonRepositoryImpl implements PokemonRepository {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemonPU");

    @Override
    public Optional<Pokemon> findById(Long id) {

        try (EntityManager em = emf.createEntityManager()) {
            Pokemon pokemon = em.find(Pokemon.class, id);
            return Optional.ofNullable(pokemon);
        }
    }

    @Override
    public List<Pokemon> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Pokemon p", Pokemon.class)
                    .getResultList();
        }
    }

    @Override
    public void save(Pokemon pokemon) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(pokemon);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
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
