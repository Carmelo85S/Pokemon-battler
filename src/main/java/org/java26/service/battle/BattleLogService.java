package org.java26.service.battle;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.java26.models.Pokemon;
import org.java26.repository.battleLogRepository.BattleLogRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BattleLogService {

    private final BattleLogRepository repository;

    public BattleLogService(BattleLogRepository repository) {
        this.repository = repository;
    }

    public void saveBattle(Pokemon player, Pokemon opponent,
                           boolean playerWon) {

        Pokemon winner = playerWon ? player : opponent;
        Pokemon loser = playerWon ? opponent : player;

        Document document = new Document()
                .append("matchId", UUID.randomUUID().toString())
                .append("playedAt", new Date())
                .append("pokemonWinnerId", winner.getId())
                .append("pokemonWinner", winner.getName())
                .append("pokemonLoserId", loser.getId())
                .append("pokemonLoser", loser.getName());

        System.out.println(document.toJson());
        repository.save(document);
    }

    public List<Document> findAll() {
        return repository.findAll();
    }

    public void printAllBattles() {
        List<Document> battles = findAll();

        JsonWriterSettings settings = JsonWriterSettings.builder()
                .indent(true)
                .indentCharacters("    ")
                .build();

        for (Document battle : battles) {
            System.out.println(battle.toJson(settings));
            System.out.println();
        }
    }

    public void showStatsBattle(Pokemon player, Pokemon opponent, boolean playerWon, List<Document> rounds) {

        Pokemon winner = playerWon ? player : opponent;
        Pokemon loser = playerWon ? opponent : player;

        Document log = new Document("matchId", UUID.randomUUID().toString())
                .append("playedAt", new Date())
                .append("participants", List.of(
                        new Document("playerId", player.getId())
                                .append("name", player.getName()),
                        new Document("opponentId", opponent.getId())
                                .append("name", opponent.getName())
                ))
                .append("rounds", rounds)
                .append("winner", new Document("pokemonId", winner.getId())
                        .append("name", winner.getName()))
                .append("loser", new Document("pokemonId", loser.getId())
                        .append("name", loser.getName()));
        repository.save(log);
    }
}
