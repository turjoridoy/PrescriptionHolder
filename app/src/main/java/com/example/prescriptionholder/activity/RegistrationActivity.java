package com.example.prescriptionholder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prescriptionholder.api.URLs;
import com.example.prescriptionholder.model.User;
import com.example.prescriptionholder.utils.RequestHandler;
import com.example.prescriptionholder.utils.SharedPrefManager;
import com.example.prescriptionholder.utils.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prescriptionholder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity{

    EditText eTname,eTphone, eTemail, eTpass,eTconfirmpass;
    Button btnReg;
    RadioGroup  radioUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        eTname = findViewById(R.id.eTname);
        eTphone = findViewById(R.id.eTphone);
        eTemail = findViewById(R.id.eTemail);
        eTpass = findViewById(R.id.eTpass);
        eTconfirmpass = findViewById(R.id.eTconfirmpass);
        radioUser = findViewById(R.id.radioUser);
        btnReg = findViewById(R.id.btnReg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });



    }

    private void registerUser() {
        final String name = eTname.getText().toString().trim();
        final String email = eTemail.getText().toString().trim();
        final String password = eTpass.getText().toString().trim();
        final String phone = eTphone.getText().toString().trim();
        boolean usertype = false;

        final String type = ((RadioButton)  findViewById(radioUser.getCheckedRadioButtonId())).getText().toString();

        if(type == "Patient"){
            usertype = false;
        }else {
            usertype = true;
        }

        //first we will do the validations

        if (TextUtils.isEmpty(name)) {
            eTname.setError("Please enter your Name");
            eTname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            eTemail.setError("Please enter your email");
            eTemail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eTemail.setError("Enter a valid email");
            eTemail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            eTpass.setError("Enter a password");
            eTpass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            eTphone.setError("Enter your phone number");
            eTphone.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                                //creating a new user object
                                User user = new User(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                        obj.getBoolean("is_doctor"),
                                        obj.getString("phone"),
                                        obj.getString("password")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                //params.put("phone", phone);
                params.put("password", password);
                //params.put("is_doctor", usertype);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
