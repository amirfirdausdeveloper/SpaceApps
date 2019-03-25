package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.space.spaceapps.MainActivity;
import com.space.spaceapps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class RegistrationActivity extends AppCompatActivity {
    LinearLayout linear_email,linear_facebook,linear_google;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        linear_email = findViewById(R.id.linear_email);
        linear_facebook = findViewById(R.id.linear_facebook);
        linear_google = findViewById(R.id.linear_google);

        linear_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),RegistrationSecondActivity.class);
                next.putExtra("account_created_from","1");
                startActivity(next);
            }
        });

        linear_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent next = new Intent(getApplicationContext(),FacebookLoginActivity.class);
                startActivity(next);
            }
        });

        linear_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),GoogleLoginActivity.class);
                startActivity(next);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(next);
    }
}
