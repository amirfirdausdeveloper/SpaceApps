package com.space.spaceapps.Activity.IntroSetting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.space.spaceapps.Activity.DashboardActivity;
import com.space.spaceapps.Activity.IntroScreenActivity;
import com.space.spaceapps.Activity.LoginActivity;
import com.space.spaceapps.R;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreen;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreenLogin;

public class IntroSettingActivity extends AppCompatActivity {
    String token = "";
    PreferenceManagerSplashScreenLogin preferenceManager;
    Button button_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_setting);

        // TO CHECK IF NOT FIRST TIME WILL GO TO SETTING SCREEN
        preferenceManager = new PreferenceManagerSplashScreenLogin(this);
        if (!preferenceManager.isFirstTimeLaunching()) {
            launchHomeScreen();
            finish();
            Log.d("masuk","masuk");
        }

        token = getIntent().getStringExtra("token");
        button_next = findViewById(R.id.button_next);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Finish this setting first",Toast.LENGTH_SHORT).show();
    }

    private void launchHomeScreen() {
        preferenceManager.setIsFirstTimeLaunch(false);
        startActivity(new Intent(IntroSettingActivity.this, DashboardActivity.class).putExtra("token",token));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //BUTTON NEXT ONCLICK
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),IntroSettingTwoActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });
    }
}
