package com.example.grpc;

import io.grpc.stub.StreamObserver;

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
    ;

    // When you are done, you must call onCompleted.
    responseObserver.onCompleted();
  }
}