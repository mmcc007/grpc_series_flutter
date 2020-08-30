(
    cd flutter_app;
    protoc --dart_out=grpc:lib/src/generated -Iproto /usr/local/Cellar/protobuf/3.12.4/include/google/protobuf/*.proto proto/GreetingService.proto

)