package org.hust.audioSearch.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.hust.audioSearch.shazam.Couple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mongo {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String collectionName = "AddressHashTable";

    public Mongo(String uri, String database) {
        mongoClient = MongoClients.create(uri);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public void insertFingerprints(Map<Integer, ArrayList<Couple>> fingerprints) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        for (Map.Entry<Integer, ArrayList<Couple>> entry : fingerprints.entrySet()) {
            List<Document> coupleDocs = new ArrayList<>();
            for (Couple couple : entry.getValue()) {
                Document coupleDoc  = new Document("SongId", couple.songId).append("Timestamp", couple.time);
                coupleDocs.add(coupleDoc);
            }
            UpdateResult result = collection.updateOne(
                    Filters.eq("_id", entry.getKey()),
                    Updates.addEachToSet("Couples", coupleDocs),
                    new UpdateOptions().upsert(true)
            );
            if (result.getUpsertedId()==null&&result.getMatchedCount()==0) System.out.printf("Unable to update hash %s \n", entry.getKey());
        }
    }

    public Map<Integer,ArrayList<Couple>> getMatchingCouples( Integer[] hashes){
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Map<Integer,ArrayList<Couple>> matchedCouples = new HashMap<>() ;
        for (int hash: hashes){
            Document matchdoc = collection.find(new Document("_id", hash)).first();
            if (matchdoc!=null){
                ArrayList<Couple> couplesForAHash = new ArrayList<>();
                ArrayList<Document> coupledocs = (ArrayList<Document>) matchdoc.get("Couples");
                for (Document coupledoc : coupledocs){
                    couplesForAHash.add(new Couple((Integer) coupledoc.get("Timestamp"), (int)coupledoc.get("SongId")));
                }
                matchedCouples.put(hash,couplesForAHash);
            }
//            else System.out.println(String.format("Can't get matched couples for hash %s", hash ));
        }
        return matchedCouples;
    }

    public void close(){
        this.mongoClient.close();
    }
}
