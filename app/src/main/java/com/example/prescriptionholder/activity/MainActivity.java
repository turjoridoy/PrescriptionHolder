package com.example.prescriptionholder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prescriptionholder.R;
import com.example.prescriptionholder.model.Prescription;
import com.example.prescriptionholder.model.User;
import com.example.prescriptionholder.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    LinearLayout add, view, profile, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        view = findViewById(R.id.view);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);

        getAllUser();
        getAllPrescriptionOfThisUser(1);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(i);

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(i);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });



    }
    public void getAllUser() {
        final ArrayList<User> userList=new ArrayList<User>();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://18.217.127.10:8009/user/api/user/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray objArray = new JSONArray(response);

                            //creating a new user object
                            for(int i=0;i<objArray.length();i++){
                                JSONObject obj=objArray.getJSONObject(i);
                                User user = new User(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                        obj.getBoolean("is_doctor"),
                                        obj.getString("password")
                                );
                                userList.add(user);
                                Log.e("user name",user.getName());
                            }

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Wrong ID / Password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



    public void getAllPrescriptionOfThisUser(int userID) {
        final ArrayList<Prescription> prescriptionsList=new ArrayList<Prescription>();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://18.217.127.10:8009/user/get/prescriptions/?user="+userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray objArray = new JSONArray(response);

                            //creating a new user object
                            for(int i=0;i<objArray.length();i++){
                                JSONObject obj=objArray.getJSONObject(i);
                                Prescription prescription = new Prescription(
                                        obj.getInt("id"),
                                        obj.getInt("user"),
                                        obj.getString("url")
                                );
                                prescriptionsList.add(prescription);
                                Log.e("prescriptionsList url",prescription.getUrl());
                            }

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Wrong ID / Password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



}