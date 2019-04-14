package com.space.spaceapps.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.space.spaceapps.R;

public class EditProfileActivity extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        token = getIntent().getStringExtra("token");
    }
}
