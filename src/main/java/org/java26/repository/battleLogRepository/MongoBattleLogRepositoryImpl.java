
package org.java26.repository.battleLogRepository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoBattleLogRepositoryImpl implements BattleLogRepository {

    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION = "battle-logs";

    public MongoBattleLogRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Document battleLogs) {
        mongoTemplate.insert(battleLogs, COLLECTION);
    }

    @Override
    public List<Document> findAll() {
        return mongoTemplate.findAll(Document.class, COLLECTION);
    }

    @Override
    public List<Document> findPokemonWinnerById(Long id) {
        Query query = new Query(
                Criteria.where("pokemonWinnerId").is(id)
        );

        return mongoTemplate.find(query, Document.class, COLLECTION);
    }

    @Override
    public boolean updateWinner(String matchId, Long newId) {
        Query query = new Query(
                Criteria.where("matchId").is(matchId)
        );

        Update update = new Update()
                .set("pokemonWinner", newId);

        return mongoTemplate.updateFirst(query, update, COLLECTION)
                .getModifiedCount() > 0;
    }

    @Override
    public long delete() {
        return mongoTemplate.remove(
                new Query(),
                COLLECTION
        ).getDeletedCount();
    }

    @Override
    public long deleteById(String matchId) {
        Query query = new Query(
                Criteria.where("matchId").is(matchId)
        );

        return mongoTemplate.remove(query, COLLECTION)
                .getDeletedCount();
    }

    @Override
    public void close() throws Exception {

    }
}
