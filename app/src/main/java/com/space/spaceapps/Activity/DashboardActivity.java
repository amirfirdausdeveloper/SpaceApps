package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.space.spaceapps.R;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout linear_edit_profile;
    String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        linear_edit_profile = findViewById(R.id.linear_edit_profile);
        token = getIntent().getStringExtra("token");

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        linear_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),EditProfileActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });
    }
}
