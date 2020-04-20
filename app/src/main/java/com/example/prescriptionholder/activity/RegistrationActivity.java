package com.example.prescriptionholder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.prescriptionholder.api.URLs;
import com.example.prescriptionholder.model.User;
import com.example.prescriptionholder.utils.RequestHandler;
import com.example.prescriptionholder.utils.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
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

public class RegistrationActivity extends AppCompatActivity{

    EditText eTname,eTphone, eTemail, eTpass,eTconfirmpass;
    Button btnReg;
    RadioGroup  radioUser;

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

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                //params.put("phone", phone);
                params.put("password", password);
                //params.put("is_doctor", usertype);


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);
                Log.e("ssss",s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

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
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}
