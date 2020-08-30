import 'package:grpc/grpc.dart';

import 'generated/GreetingService.pbgrpc.dart';

class Client {
  ClientChannel channel;
  GreetingServiceClient stub;

  Client({String server = '127.0.0.1', int port = 8080}) {
    channel = ClientChannel(server,
        port: port,
        options:
            const ChannelOptions(credentials: ChannelCredentials.insecure()));
    stub = GreetingServiceClient(channel,
        options: CallOptions(timeout: Duration(seconds: 30)));
  }

  Future<String> getGreeting(String name) async {
    // return the last streamed greeting
    String greeting;

    try {
      final request = HelloRequest();
      request.name = name;
      await for (var response in stub.greeting(request)) {
        print(response);
        greeting = response.greeting;
      }
    } catch (e) {
      print('Caught error: $e');
    }
    return greeting;
  }

  Future<void> close() async {
    await channel.shutdown();
  }
}
