package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.space.spaceapps.R;

public class DashboardActivity extends AppCompatActivity {

    String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

       /* token = getIntent().getStringExtra("token");*/
    }

    @Override
    public void onBackPressed() {

    }
}
