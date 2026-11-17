package org.java26.repository;

import org.java26.models.Pokemon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PokemonRepository {

    Optional<Pokemon> findById(Long id);
    List<Pokemon> findAll();
    List<Pokemon> findByName(String name);
    void save(Pokemon pokemon);
    void delete(Long id);
    void deleteByName(String name);

    void updateLevel(Pokemon pokemon);
}
