package com.space.spaceapps.Activity.IntroSetting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.space.spaceapps.Activity.DashboardActivity;
import com.space.spaceapps.R;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreen;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreenLogin;

import io.apptik.widget.MultiSlider;

public class IntroSettingFourActivity extends AppCompatActivity {

    TextView textView_sq_max,textView_sq_min;
    MultiSlider multiSlider;

    Button button_back,button_next;
    String token;
    PreferenceManagerSplashScreenLogin preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_setting_four);

        preferenceManager = new PreferenceManagerSplashScreenLogin(this);
        token = getIntent().getStringExtra("token");

        multiSlider = findViewById(R.id.range_slider5);
        textView_sq_max = findViewById(R.id.textView_sq_max);
        textView_sq_min = findViewById(R.id.textView_sq_min);
        button_back = findViewById(R.id.button_back);
        button_next = findViewById(R.id.button_next);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //SET MAX AND MINIMU FOR SEEKBAR
        multiSlider.setMin(1000);
        multiSlider.setMax(10000);
        //END
    }

    @Override
    protected void onResume() {
        super.onResume();

        //ON CHANGE FOR SEEKBAR
        multiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex,int value)
            {
                if (thumbIndex == 0) {
                    textView_sq_min.setText(String.valueOf(value)+" sq.ft");
                } else {
                    textView_sq_max.setText(String.valueOf(value)+" sq.ft");
                }
            }
        });
        //END

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager.setIsFirstTimeLaunch(false);
                Intent next = new Intent(getApplicationContext(), DashboardActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), IntroSettingThreeActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Finish this setting first",Toast.LENGTH_SHORT).show();
    }
}
