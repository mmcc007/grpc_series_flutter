package com.example.grpc;

import io.grpc.*;
// New import
import io.grpc.stub.*;

public class Client {
  public static void main(String[] args) throws Exception {
    final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext(true).build();

    // Replace the previous synchronous code with asynchronous code.
    // This time use an async stub:
    GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

    // Construct a request
    GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder().setName("Ray")
        .build();

    // Make an Asynchronous call. Listen to responses w/ StreamObserver
    stub.greeting(request, new StreamObserver<GreetingServiceOuterClass.HelloResponse>() {
      public void onNext(GreetingServiceOuterClass.HelloResponse response) {
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
    GreetingServiceOuterClass.ListDbRequest listDatabasesRequest = GreetingServiceOuterClass.ListDbRequest.newBuilder()
        .build();

    // Finally, make the call using the stub
    // GreetingServiceOuterClass.ListDbResponse response =
    // stub.listDatabases(listDatabasesRequest);
    // stub.listDatabases(request, responseObserver);

    stub.listDatabases(listDatabasesRequest, new StreamObserver<GreetingServiceOuterClass.ListDbResponse>() {
      public void onNext(GreetingServiceOuterClass.ListDbResponse response) {
        System.out.println(response);
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

  }
}