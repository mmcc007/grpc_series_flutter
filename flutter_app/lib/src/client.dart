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
    try {
      final request = HelloRequest();
      request.name = name;
      final response = await stub.greeting(request);
      return response.greeting;
    } catch (e) {
      print('Caught error: $e');
    }
    return null;
  }

  Future<List<String>> listDatabases() async {
    final response = await stub.listDatabases(ListDbRequest());
    return response.database;
  }

  Future<void> close() async {
    await channel.shutdown();
  }
}
