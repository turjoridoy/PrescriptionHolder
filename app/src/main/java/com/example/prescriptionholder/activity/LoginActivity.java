package com.example.prescriptionholder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prescriptionholder.R;
import com.example.prescriptionholder.api.URLs;
import com.example.prescriptionholder.model.User;
import com.example.prescriptionholder.utils.RequestHandler;
import com.example.prescriptionholder.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText eTemail, eTpass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        eTemail = findViewById(R.id.eTemail);
        eTpass = findViewById(R.id.eTpass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGETRequest("http://18.217.127.10:8009/user/login/?email="+eTemail.getText().toString()+"&password="+eTpass.getText().toString());

            }

        });
    }


    public void sendGETRequest(String requestURL) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            //creating a new user object
                            User user = new User(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getString("email"),
                                    obj.getBoolean("is_doctor"),
                                    obj.getString("password")
                            );

                            //storing the user in shared preferences
                            Log.e("here",user.getEmail());
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }catch (Exception e){
                            Toast.makeText(LoginActivity.this, "Wrong ID / Password !", Toast.LENGTH_SHORT).show();
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
