import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_app/src/helper.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_app/src/client.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Google Maps Demo',
      home: MapSample(),
    );
  }
}

class MapSample extends StatefulWidget {
  @override
  State<MapSample> createState() => MapSampleState();
}

class MapSampleState extends State<MapSample> {
  Completer<GoogleMapController> _controller = Completer();
  Client client = Client(server: '192.168.0.254');
  final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  Set<Polygon> _polygons = {};
  final _neighborhood = 'Midtown-Midtown South';

  static final CameraPosition _kNeighborhood = CameraPosition(
    target: LatLng(40.7550, -73.9835),
    zoom: 14,
  );

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      key: _scaffoldKey,
      body: GoogleMap(
        mapType: MapType.normal,
        initialCameraPosition: _kNeighborhood,
        onMapCreated: (GoogleMapController controller) {
          _showNeighborhood();
          _controller.complete(controller);
        },
        onTap: _tapMap,
        polygons: _polygons,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _goToNeighborhood,
        child: Icon(Icons.home),
      ),
    );
  }

  _showNeighborhood() async {
    final result = await client.getNeighborhood(_neighborhood);
    final points = result.map((e) => cnvLagLngGrpcToMap(e)).toList();
    print(points);
    _polygons = {
      Polygon(
        polygonId: PolygonId('neighborhood'),
        points: points,
        fillColor: Colors.black12,
        strokeColor: Colors.black,
        strokeWidth: 2,
        // fillColor: Colors.green.withOpacity(0.5),
      )
    };
    setState(() {});
  }

  _tapMap(LatLng latLng) async {
    print(latLng);
    final isInPolygon =
        await client.isInPolygon(cnvLatLngMapToGrpc(latLng), _neighborhood);
    String message;
    if (isInPolygon)
      message = '$latLng is in neighborhood';
    else
      message = '$latLng is NOT in neighborhood';
    _scaffoldKey.currentState.showSnackBar(SnackBar(
      content: Text(message),
      duration: Duration(seconds: 3),
    ));
  }

  _goToNeighborhood() async {
    print('go');
    // _showNeighborhood();
    final GoogleMapController controller = await _controller.future;
    controller.animateCamera(CameraUpdate.newCameraPosition(_kNeighborhood));
  }
}
