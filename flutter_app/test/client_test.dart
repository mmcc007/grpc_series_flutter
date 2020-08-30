import 'package:flutter_app/src/client.dart';
import 'package:flutter_test/flutter_test.dart';

main() {
  group('gRPC Client', () {
    Client client;

    setUp(() {
      client = Client();
    });

    tearDown(() {
      client.close();
    });

    test('hello', () async {
      final result = await client.getGreeting('counter');
      expect(result, 'Hello there, counter');
    });
  });
}
