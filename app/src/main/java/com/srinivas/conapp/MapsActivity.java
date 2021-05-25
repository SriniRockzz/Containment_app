package com.srinivas.conapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.DebugUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.util.Listener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private DatabaseReference mDatabase;

    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

public List<LatLng> latlng= new ArrayList<LatLng>();
    private float GEOFENCE_RADIUS = 100;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }


    /**
     * db.collection("latlong").get().addOnCompleteListener(task -> {
     *             if (task.isSuccessful()) {
     *                 Log.d("qwerty", "succes");
     *                 for (QueryDocumentSnapshot document : task.getResult()) {
     *                     Log.d("qwerty", document.getId() + " => " + document.getData());
     * Double n2=document.getDouble("latitude");
     *                     Double n1=document.getDouble("longitude");
     *                     Log.d("value1", n1.toString());
     *                     LatLng n= new LatLng(n2,n1);
     *                     latlng.add(n);
     *                 }
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //db.collection("cities")
    //        .whereEqualTo("state", "CA")
    //        .addSnapshotListener(new EventListener<QuerySnapshot>() {
    //            @Override
    //            public void onEvent(@Nullable QuerySnapshot snapshots,
    //                                @Nullable FirebaseFirestoreException e) {
    //                if (e != null) {
    //                    Log.w(TAG, "listen:error", e);
    //                    return;
    //                }
    //
    //                for (DocumentChange dc : snapshots.getDocumentChanges()) {
    //                    switch (dc.getType()) {
    //                        case ADDED:
    //                            Log.d(TAG, "New city: " + dc.getDocument().getData());
    //                            break;
    //                        case MODIFIED:
    //                            Log.d(TAG, "Modified city: " + dc.getDocument().getData());
    //                            break;
    //                        case REMOVED:
    //                            Log.d(TAG, "Removed city: " + dc.getDocument().getData());
    //                            break;
    //                    }
    //                }
    //
    //            }
    //        });

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();
        background1();

        // Add a marker in Sydney and move the camera


// Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        db.collection("latlong").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("qwerty", "succes");

                    for (QueryDocumentSnapshot document : task.getResult()) {

Double n2=document.getDouble("latitude");
                        Double n1=document.getDouble("longitude");

                        LatLng n= new LatLng(n2,n1);
                        latlng.add(n);

                    }Log.d("qwerty", String.valueOf(latlng));
                    if(latlng!=null && latlng.size()>0) {

                        addGeofence(latlng, GEOFENCE_RADIUS);}

                } else {
                    Log.d("qwerty", "Error getting documents: ", task.getException());
                }
            }
        });
        db.collection("latlong")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Double n2=dc.getDocument().getDouble("latitude");
                                    Double n1=dc.getDocument().getDouble("longitude");
                                    Log.i("qwerty",n1.toString());
                                    LatLng n= new LatLng(n2,n1);
                                    latlng.add(n);
                                    addGeofence(latlng,GEOFENCE_RADIUS);
                                    break;
                                case REMOVED:
                                    Double n22=dc.getDocument().getDouble("latitude");
                                    Double n11=dc.getDocument().getDouble("longitude");

                                    LatLng n5= new LatLng(n22,n11);
                                    int i=0;
                                    while(i<latlng.size())
                                    {
                                        if(latlng.get(i).equals(n5)){
                                            latlng.remove(i);
                                        }
                                    }
                                    addGeofence(latlng,GEOFENCE_RADIUS);
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });


}




    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }


    }
    public void background1(){

            //We need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //We have the permission
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    //We do not have the permission..

                }
            }

            if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //We have the permission
                    Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
                } else {
                    //We do not have the permission..
                    Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
                }
            }
        }





    private void addGeofence(List<LatLng> latLng, float radius) {


       List <Geofence> geofence = new ArrayList<>();

        addMarker(latLng);
        addCircle(latLng, radius);
        int i=0;
                       while(i<latLng.size()){
             geofence.add(  geofenceHelper.getGeofence(String.valueOf(i+1), latLng.get(i), radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL) );
               i++;}
                       GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();






        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }

    private void addMarker(List<LatLng> latLng) {

        int i = 0;
        while (i<latLng.size()) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng.get(i));
            mMap.addMarker(markerOptions);
            i++;
        }
    }

    private void addCircle(List<LatLng> latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        int i=0;
        while(i<latLng.size()) {
            circleOptions.center(latLng.get(i));
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 0, 0,0));
            circleOptions.fillColor(Color.argb(64, 0, 0,0));
            circleOptions.strokeWidth(4);
            mMap.addCircle(circleOptions);
            i++;
        }

    }
    private  void updatedGeofence(){

    }
}