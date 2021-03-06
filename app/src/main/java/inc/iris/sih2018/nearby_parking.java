package inc.iris.sih2018;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import inc.iris.sih2018.logic.NearByComparator;

/**
 * Created by lappy on 3/26/2018.
 */

public class nearby_parking extends AppCompatActivity {
    private List<nearby_parking_bean> mParkings = new ArrayList<>();
    private nearby_parking_adapter mParkingAdapter;
    RecyclerView recyclerView;
    private Toolbar toolbar;
    RequestQueue queue;
    Context context=this;
    JSONObject details;
    String park_name;
    int avail_slots,cost;
    double latitude;
    double longitude;
    String distance;
    double latitude_user;
    double longitude_user;
    private static final String TAG = "nearby_parking";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_parking);
        queue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_booking);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("NearBy Parkings");
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mParkingAdapter = new nearby_parking_adapter(mParkings, this);
        Intent intent;
        intent=getIntent();
        latitude_user=intent.getDoubleExtra("latitude",0);
        longitude_user=intent.getDoubleExtra("longitude",0);
        Log.d(TAG, "onCreate: lat "+latitude_user+" lng "+longitude_user);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://sih2018.esy.es/parking.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response);
                try {
                   JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("server_response");
                    DecimalFormat df=new DecimalFormat("#.##");
                   // Log.d("aaa", String.valueOf(jsonArray.length()));
                    for(int i=0;i<jsonArray.length();i++) {
                        details = (JSONObject) jsonArray.get(i);
                        park_name = details.getString("park_name");
                        avail_slots = details.getInt("available_slots");
                        latitude = details.getDouble("latitude");
                        longitude = details.getLong("longitude");

                        distance=df.format(details.getDouble("distance"));
                        cost=details.getInt("cost");
                        nearby_parking_bean mybean1 = new nearby_parking_bean(park_name,distance, avail_slots,cost);
                        mParkings.add(mybean1);

                    }
                    Collections.sort(mParkings,new NearByComparator());
                    mParkingAdapter = new nearby_parking_adapter(mParkings, nearby_parking.this);
                    recyclerView.setAdapter(mParkingAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("latitude", String.valueOf(latitude_user));
                params.put("longitude", String.valueOf(longitude_user));
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
