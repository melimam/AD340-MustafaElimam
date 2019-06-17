package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class TrafficMap extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;
    public LocationCallback mLocationCallback;
    private static boolean wifiConnected = false;

    // Whether there is a mobile connection.

    private static boolean mobileConnected = false;

    private Location mLastLocation;
    private GoogleMap mMap;

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9;
    protected boolean mAddressRequested;
    protected String mAddressOutput;


    protected TextView mLocationText;

    List<TrafficCamera> cameraData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkNetworkConnection();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mAddressRequested = true;
        mAddressOutput = "";
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocations();


    }

    private void createLocationCallback() {

        mLocationCallback = new LocationCallback() {

            @Override

            public void onLocationResult(LocationResult locationResult) {

                Log.d("LOCATION", "onLocationResult");

                if (locationResult == null) {

                    return;

                }

                for (Location location : locationResult.getLocations()) {

                    mLastLocation = location;

                    updateUI();

                }

            }

            ;

        };

    }


    public void getLocations() {
        Log.d("Location", "getLocations");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Location", "PermissionGranted");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        mLastLocation = location;
                        updateUI();
                    }
                }
            });
        } else {
            Log.d("Location", "PermissionGranted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // logic

            } else {
                Log.d("Location", "requestpermission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("Location", "onRequestPermissionsResults");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateUI();
                } else {
                    //err

                }
                return;
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("location", "onMapReady");
        mMap = googleMap;


        loadcameradata(dataUrl);
    }

    ;
    String dataUrl = "http://brisksoft.us/ad340/traffic_cameras_merged.json";

    public void loadcameradata(String dataUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET, dataUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("CAMERAS 1", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject camera = response.getJSONObject(i);
                        double[] coordinates = {camera.getDouble("ypos"), camera.getDouble("xpos")};


                        TrafficCamera c = new TrafficCamera(camera.getString("cameralabel"), camera.getJSONObject("imageurl").getString("url"), camera.getString("ownershipcd"), coordinates);
                        cameraData.add(c);
                        Log.i("Camera data", c.toString());

                    }
                    showMarkers();


                    //listAdapter.notifyDataSetChanged();
                    // if(int i=0; cameraData.size() > 1; i++)

                } catch (JSONException e) {
                    Log.d("CAMERAS error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });
        queue.add(jsonReq);
    }

    ;

    public void updateUI() {
        if (mLastLocation == null) {
            //
        } else {
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.setMinZoomPreference(12);
            mMap.addMarker(new MarkerOptions().position(myLocation).title(" my current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        }
    }


    public void showMarkers() {

        Log.i("CAMERA", cameraData.toString());


        for (TrafficCamera camera : cameraData) {
            Log.i("CAMERA DATA", camera.toString());
            LatLng position = new LatLng(camera.coordinates[0], camera.coordinates[1]);
            Marker m = mMap.addMarker(new MarkerOptions().position(position).title(camera.label).snippet(camera.image));
            m.setTag(camera);
        }
    }

    private void checkNetworkConnection() {

        // BEGIN_INCLUDE(connect)

        ConnectivityManager connMgr =

                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {

            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;

            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (wifiConnected) {

                Log.i("Network", "wifi conneciton");

            } else if (mobileConnected) {

                Log.i("Network", "mobile connection");

            }

        } else {

            Log.i("Network", "no network connection");

        }

        // END_INCLUDE(connect)

    }

}