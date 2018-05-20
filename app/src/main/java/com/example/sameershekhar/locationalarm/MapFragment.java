package com.example.sameershekhar.locationalarm;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.POJO.Example;
import com.example.sameershekhar.locationalarm.Services.MyService;
import com.example.sameershekhar.locationalarm.data.Alarm;
import com.example.sameershekhar.locationalarm.data.DataBaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sameershekhar.locationalarm.MainActivity.origin;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements FragmentLifecycle {
    List<LatLng> locationArrayList = new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    Polyline line;
    public MapFragment() {
        // Required empty public constructor
    }

    private MarkerOptions options = new MarkerOptions();
    private MapView mMapView;
    private GoogleMap googleMap;
    private TextView dis,time;

    private List<Alarm> alarmArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        dataBaseHelper=new DataBaseHelper(getContext());
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        dis=(TextView)rootView.findViewById(R.id.dist);
        time=(TextView)rootView.findViewById(R.id.time);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();



        // locationArrayList.add() dataBaseHelper


    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private boolean checkPermission() {
        Log.d("TAG", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d("TAG", "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1
        );

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TAG", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission())
                        googleMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied

                }
                break;
            }
        }
    }

    @Override
    public void onPauseFragment() {
        Log.i("TAG", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + "TAG", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {

        Log.i("TAG", "onResumeFragment()");
        //Toast.makeText(getActivity(), "onResumeFragment():" + "TAG", Toast.LENGTH_SHORT).show();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                int height = 80;
                int width = 80;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.currentlocation);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                googleMap.clear();
                MarkerOptions marker = new MarkerOptions().position(new LatLng(MainActivity.origin.latitude, MainActivity.origin.longitude));
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                googleMap.addMarker(marker);

                if (checkPermission())
                    googleMap.setMyLocationEnabled(true);
                else askPermission();

                if(Geocoder.isPresent()){
                    try {
                        String location="";
                        String description="";
                        alarmArrayList=MainActivity.checkedAlarm;

                        int size=alarmArrayList.size();
                        if(size!=0) {
                            location = alarmArrayList.get(0).getLocation();
                                Geocoder gc = new Geocoder(getContext());
                                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects
                                List<LatLng> latLngArrayList = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                                for(Address a : addresses){
                                    if(a.hasLatitude() && a.hasLongitude()){
                                      LatLng dest= new LatLng(a.getLatitude(), a.getLongitude());
                                        //LatLng origin=new LatLng(28.708057,77.117954);

                                        Log.d("X1"," "+dest.latitude+dest.longitude);
                                        build_retrofit_and_get_response(MainActivity.origin,dest);

                                        }

                        }
                            //}
                        }
                    } catch (IOException e) {
                        // handle the exception
                    }
                }
                }
        });



    }

    private void build_retrofit_and_get_response(LatLng origin, final LatLng dest) {


        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, "driving");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response ) {
                try {

                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String tim = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        //Toast.makeText(getContext(),distance+" ,"+time,Toast.LENGTH_LONG).show();
                        //Log.d("dis&time",distance+", "+tim);
                        dis.setText(distance);
                        time.setText(tim);
                        //ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);

                        line = googleMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(10)
                                .color(Color.GREEN)
                                .geodesic(true)


                        );
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(dest.latitude, dest.longitude)).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(dest.latitude, dest.longitude));
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        googleMap.addMarker(marker);
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
