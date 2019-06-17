package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;



import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;


public class traffic_camera extends AppCompatActivity {
    private static boolean wifiConnected = false;

    // Whether there is a mobile connection.

    private static boolean mobileConnected = false;
    ListView cameraList;
    CameraListAdapter listAdapter;













    String dataurl = "http://brisksoft.us/ad340/traffic_cameras_merged.json";
    ArrayList<TrafficCamera> cameraData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        cameraList = findViewById(R.id.camera_list);
        listAdapter = new CameraListAdapter(this, cameraData);
        cameraList.setAdapter(listAdapter);

        checkNetworkConnection();
        //wifiConnected = false;
        if (wifiConnected || mobileConnected) {

            // inatiate data request
            loadCameraData(dataurl);
        } else {
            // show error
        }
    }
    public class CameraListAdapter extends ArrayAdapter<TrafficCamera> {

        private final Context context;
        private ArrayList<TrafficCamera> values;
        public CameraListAdapter(Context context,ArrayList<TrafficCamera> values){
            super(context, 0,values);
            this.context = context;
            this.values = values;


        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.traffic_data, parent, false);
            TextView label = rowView.findViewById(R.id.label);
            ImageView image = rowView.findViewById(R.id.image);
            label.setText(values.get(position).label);
            String imageurl = values.get(position).image;
            if(!imageurl.isEmpty()){
                Picasso.get().load(imageurl).into(image);


            }

            return rowView;

        }





    }
    public void loadCameraData(String dataUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonReq = new JsonArrayRequest
                (Request.Method.GET, dataUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("CAMERAS 1", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject camera = response.getJSONObject(i);

                                double[] coords = {camera.getDouble("ypos"), camera.getDouble("xpos")};

                                TrafficCamera c = new TrafficCamera(
                                        camera.getString("cameralabel"),
                                        camera.getJSONObject("imageurl").getString("url"),
                                        camera.getString("ownershipcd"),
                                        coords
                                );
                                cameraData.add(c);

                            }
                            listAdapter.notifyDataSetChanged();

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
    private void checkNetworkConnection() {

        // BEGIN_INCLUDE(connect)

        ConnectivityManager connMgr =

                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {

            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;

            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnected) {

                Log.i("Network", "wifi conneciton");

            } else if (mobileConnected){

                Log.i("Network", "mobile connection");

            }

        } else {

            Log.i("Network", "no network connection");

        }

        // END_INCLUDE(connect)

    }



}