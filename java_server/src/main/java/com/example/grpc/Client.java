package com.example.grpc;

import com.example.grpc.GreetingServiceGrpc.GreetingServiceStub;
import com.example.grpc.GreetingServiceOuterClass.HelloRequest;
import com.example.grpc.GreetingServiceOuterClass.HelloResponse;
import com.example.grpc.GreetingServiceOuterClass.IsInPolygonRequest;
import com.example.grpc.GreetingServiceOuterClass.IsInPolygonResponse;
import com.example.grpc.GreetingServiceOuterClass.LatLng;
import com.example.grpc.GreetingServiceOuterClass.ListDbRequest;
import com.example.grpc.GreetingServiceOuterClass.ListDbResponse;
import com.example.grpc.GreetingServiceOuterClass.NeighborhoodRequest;
import com.example.grpc.GreetingServiceOuterClass.NeighborhoodResponse;

import io.grpc.*;
// New import
import io.grpc.stub.*;

// https://codelabs.developers.google.com/codelabs/cloud-grpc-java/#0
public class Client {
  public static void main(String[] args) throws Exception {
    final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext(true).build();

    // Replace the previous synchronous code with asynchronous code.
    // This time use an async stub:
    GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

    // Construct a request
    HelloRequest request = HelloRequest.newBuilder().setName("Ray").build();

    // Make an Asynchronous call. Listen to responses w/ StreamObserver
    stub.greeting(request, new StreamObserver<HelloResponse>() {
      public void onNext(HelloResponse response) {
        System.out.println(response);
      }

      public void onError(Throwable t) {
      }

      public void onCompleted() {
        // Typically you'll shutdown the channel somewhere else.
        // But for the purpose of the lab, we are only making a single
        // request. We'll shutdown as soon as this request is done.
        // channel.shutdownNow();
      }
    });

    // list the MongoDB databases
    // https://www.mongodb.com/blog/post/quick-start-java-and-mongodb--starting-and-setup?utm_campaign=javaquickstart
    ListDbRequest listDatabasesRequest = ListDbRequest.newBuilder().build();

    stub.listDatabases(listDatabasesRequest, new StreamObserver<ListDbResponse>() {
      public void onNext(ListDbResponse response) {
        // System.out.println(response);
      }

      public void onError(Throwable t) {
      }

      public void onCompleted() {
        // Typically you'll shutdown the channel somewhere else.
        // But for the purpose of the lab, we are only making a single
        // request. We'll shutdown as soon as this request is done.
        // channel.shutdownNow();
      }
    });

    NeighborhoodRequest neighborhoodRequest = NeighborhoodRequest.newBuilder().build();
    stub.getNeighborhood(neighborhoodRequest, new StreamObserver<NeighborhoodResponse>() {
      public void onNext(NeighborhoodResponse response) {
        for (LatLng point : response.getPolygonList()) {
          // System.out.println(point);
        }
      }

      public void onError(Throwable t) {
      }

      public void onCompleted() {
        // Typically you'll shutdown the channel somewhere else.
        // But for the purpose of the lab, we are only making a single
        // request. We'll shutdown as soon as this request is done.
        channel.shutdownNow();
      }
    });

    LatLng latLng = LatLng.newBuilder().setLatitude("40.7020516665144").setLongitude("-73.95255052777945").build();
    IsInPolygonRequest isInPolygonRequest = IsInPolygonRequest.newBuilder().setPoint(latLng).setPolygonName("Bedford")
        .build();

    stub.isInPolygon(isInPolygonRequest, new StreamObserver<IsInPolygonResponse>() {
      public void onNext(IsInPolygonResponse response) {
        System.out.println(response.getIsInPolygon());
      }

      public void onError(Throwable t) {
      }

      public void onCompleted() {
        // Typically you'll shutdown the channel somewhere else.
        // But for the purpose of the lab, we are only making a single
        // request. We'll shutdown as soon as this request is done.
        // channel.shutdownNow();
      }
    });
  }
}