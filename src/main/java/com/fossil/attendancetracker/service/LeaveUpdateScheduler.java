package com.fossil.attendancetracker.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Updates.inc;

@Service
public class LeaveUpdateScheduler {

    @Autowired
    MongoClient client;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateLeaves() {
        System.out.println("Scheduler Called");
        MongoDatabase database = client.getDatabase("digital-GBS");
        MongoCollection<Document> collection = database.getCollection("users");

        collection.updateMany(new Document(), inc("leave", 2.5));
    }
}
