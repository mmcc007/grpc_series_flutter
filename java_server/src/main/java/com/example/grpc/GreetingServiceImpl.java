package com.example.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.grpc.GreetingServiceOuterClass.LatLng;
import com.example.grpc.GreetingServiceOuterClass.ListDbResponse;
import com.example.grpc.GreetingServiceOuterClass.NeighborhoodResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import io.grpc.stub.StreamObserver;

// https://codelabs.developers.google.com/codelabs/cloud-grpc-java
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
        @Override
        public void greeting(GreetingServiceOuterClass.HelloRequest request,
                        StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
                // HelloRequest has toString auto-generated.
                System.out.println(request);

                // You must use a builder to construct a new Protobuffer object
                GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                                .setGreeting("Hello there, " + request.getName()).build();

                // Feel free to construct different responses if you'd like.
                responseObserver.onNext(response);
                responseObserver.onNext(response);
                responseObserver.onNext(response);

                // When you are done, you must call onCompleted.
                responseObserver.onCompleted();
        }

        // https://www.mongodb.com/blog/post/quick-start-java-and-mongodb--starting-and-setup?utm_campaign=javaquickstart
        @Override
        public void listDatabases(GreetingServiceOuterClass.ListDbRequest request,
                        StreamObserver<GreetingServiceOuterClass.ListDbResponse> responseObserver) {
                String connectionString = System.getProperty("mongodb.uri");
                try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                        List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
                        List<String> databasesStrList = databases.stream()
                                        .map(database -> new String(database.toJson())).collect(Collectors.toList());
                        ListDbResponse response = GreetingServiceOuterClass.ListDbResponse.newBuilder()
                                        .addAllDatabase(databasesStrList).build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void getNeighborhood(GreetingServiceOuterClass.NeighborhoodRequest request,
                        StreamObserver<GreetingServiceOuterClass.NeighborhoodResponse> responseObserver) {

                String connectionString = System.getProperty("mongodb.uri");
                try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                        MongoDatabase db = mongoClient.getDatabase("sample_restaurants");
                        MongoCollection<Document> collection = db.getCollection("neighborhoods");
                        Document neighborhood = collection.find().first();
                        // for (Map.Entry<String, Object> entry : neighborhood.entrySet()) {
                        // System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                        // }
                        Document geometry = (Document) neighborhood.get("geometry");
                        @SuppressWarnings("unchecked")
                        List<List<List<Double>>> coordinates = (List<List<List<Double>>>) geometry.get("coordinates");
                        List<LatLng> polygon = coordinates.get(0).stream()
                                        .map(coords -> LatLng.newBuilder().setLatitude(coords.get(1).toString())
                                                        .setLongitude(coords.get(0).toString()).build())
                                        .collect(Collectors.toList());
                        NeighborhoodResponse response = GreetingServiceOuterClass.NeighborhoodResponse.newBuilder()
                                        .addAllPolygon(polygon).build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace(System.err);
                }

        }

}