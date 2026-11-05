package org.java26.repository;

import org.java26.models.Pokemon;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository {

    Optional<Pokemon> findById(int id);
    List<Pokemon> findAll();
    void save(Pokemon pokemon);
    void delete(int id);
}
