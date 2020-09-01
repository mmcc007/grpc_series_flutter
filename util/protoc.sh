#!/bin/bash
(
    cd flutter_app
    generated='lib/src/generated'
    rm -rf $generated
    mkdir -p $generated
    protoc --dart_out=grpc:$generated -Iproto /usr/local/Cellar/protobuf/3.12.4/include/google/protobuf/*.proto proto/GreetingService.proto

)