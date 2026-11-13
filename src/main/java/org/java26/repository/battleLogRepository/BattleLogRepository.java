package org.java26.repository.battleLogRepository;

import org.bson.Document;

import java.util.List;

public interface BattleLogRepository extends AutoCloseable {
    void save(Document battleLogs);

    List<Document> findAll();

    List<Document> findPokemonWinnerById(Long id);

    boolean updateWinner(String matchId, Long newId);

    long delete();

    long deleteById(String matchId);

    @Override
    void close();
}
