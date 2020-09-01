import 'package:google_maps_flutter/google_maps_flutter.dart' as map;

import 'generated/GreetingService.pb.dart' as grpc;

grpc.LatLng cnvLatLngMapToGrpc(map.LatLng latLng) => grpc.LatLng()
  ..latitude = latLng.latitude.toString()
  ..longitude = latLng.longitude.toString();

map.LatLng cnvLagLngGrpcToMap(grpc.LatLng latLng) =>
    map.LatLng(double.parse(latLng.latitude), double.parse(latLng.longitude));
