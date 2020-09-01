import 'package:flutter_app/src/client.dart';
import 'package:flutter_app/src/generated/GreetingService.pb.dart';
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

    test('list databases', () async {
      final result = await client.listDatabases();
      expect(result, isNot(null));
    });

    test('get neighborhood', () async {
      final result = await client.getNeighborhood('Bedford');
      expect(result.length, greaterThan(0));
    });

    test('isInPolygon', () async {
      final latLng = LatLng();
      latLng.latitude = '40.7020516665144';
      latLng.longitude = '-73.95255052777945';
      final polygonName = 'Bedford';
      final result = await client.isInPolygon(latLng, polygonName);
      expect(result, true);
    });

    test('not isInPolygon', () async {
      final latLng = LatLng();
      latLng.latitude = '40.7021';
      latLng.longitude = '-73.95256';
      final polygonName = 'Bedford';
      final result = await client.isInPolygon(latLng, polygonName);
      expect(result, false);
    });
  });
}
