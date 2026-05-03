package org.example.proyectotdextraccion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConexion {

    private static final String URI = "mongodb://localhost:27017";
    private static final MongoClient client = MongoClients.create(URI);

    public static MongoDatabase getDatabase() {
        return client.getDatabase("etl_aeronautico");
    }
}
