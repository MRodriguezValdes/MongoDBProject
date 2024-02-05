package org.mongoproject;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnector() {
        // Conexión con la instancia local de MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017");

        // Selección de la base de datos "reservesDB"
        database = mongoClient.getDatabase("reservesDB");
    }

    // Método para obtener la base de datos
    public MongoDatabase getDatabase() {
        return database;
    }

    // Método para cerrar la conexión con MongoDB
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}