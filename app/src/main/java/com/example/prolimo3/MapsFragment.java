package com.example.prolimo3;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference db;
    private ArrayList<Marker> temRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    Map<String,Object> userLocation = new HashMap<>();
    LatLng sydney;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;


            db.child("usuario").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(Marker marker:realTimeMarkers){

                        marker.remove();
                    }

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        MapsLocal ml = snapshot.getValue(MapsLocal.class);
                        Double Latitude= ml.getLatitude();
                        Double Longitude = ml.getLongitude();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(Latitude,Longitude));
                        temRealTimeMarkers.add(mMap.addMarker(markerOptions));


                    }

                    realTimeMarkers.clear();
                    realTimeMarkers.addAll(temRealTimeMarkers);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // LatLng sydney = new LatLng(-34, 151);
            //   googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
        //start

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db= FirebaseDatabase.getInstance().getReference(); //get FireBase reference

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) this.getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("Latitude: ",+location.getLatitude()+" Longitude: "+location.getLongitude());

                            //get user location and save it into userLocation HashMap
                            userLocation.put("Latitude",location.getLatitude());
                            userLocation.put("Longitude",location.getLongitude());
                            sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            //push userLocation HashMap into FireBase
                            db.child("usuario").push().setValue(userLocation);

                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



}