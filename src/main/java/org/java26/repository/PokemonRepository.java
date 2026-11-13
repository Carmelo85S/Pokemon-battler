package org.java26.repository;

import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository {

    Optional<Pokemon> findById(Long id);
    List<Pokemon> findAll();
    List<Pokemon> findByName(String name);
    void save(Pokemon pokemon);
    void delete(Long id);
    void deleteByName(String name);

    void updateLevel(Pokemon pokemon);
}
