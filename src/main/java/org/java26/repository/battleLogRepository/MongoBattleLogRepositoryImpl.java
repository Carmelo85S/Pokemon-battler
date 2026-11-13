package org.java26.repository.battleLogRepository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.java26.config.MongoConfig;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoBattleLogRepositoryImpl implements BattleLogRepository{

    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public MongoBattleLogRepositoryImpl(){
        client= MongoClients.create(
                MongoConfig.getConnectionString()
        );
        MongoDatabase database =
                client.getDatabase(MongoConfig.getDatabaseName());
        collection = database.getCollection(
                MongoConfig.getCollectionName()
        );
    };

    @Override
    public void save(Document battleLogs) {
        collection.insertOne(battleLogs);
    }

    @Override
    public List<Document> findAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public List<Document> findPokemonWinnerById(Long id) {
        return collection.find(
                eq("pokemonWinnerId", id)
        ).into(new ArrayList<>());
    }

    @Override
    public boolean updateWinner(String matchId, Long newId) {
        return collection.updateOne(
                eq("matchId", matchId),
                set("pokemonWinner", newId)
        ).getModifiedCount() > 0;
    }

    @Override
    public long delete() {
        return collection.deleteMany(new Document()).getDeletedCount();
    }

    @Override
    public long deleteById(String matchId) {
        return collection.deleteOne(
                eq("matchId", matchId)
        ).getDeletedCount();
    }

    @Override
    public void close() {
        client.close();
    }


}
