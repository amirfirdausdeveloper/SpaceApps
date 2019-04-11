package com.space.spaceapps.Activity.IntroSetting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.space.spaceapps.R;

import io.apptik.widget.MultiSlider;

public class IntroSettingTwoActivity extends AppCompatActivity {

    TextView textView_price_max,textView_price_min;
    MultiSlider multiSlider;
    String token;
    Button button_next,button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_setting_two);

        token = getIntent().getStringExtra("token");

        multiSlider = findViewById(R.id.range_slider5);
        textView_price_max = findViewById(R.id.textView_price_max);
        textView_price_min = findViewById(R.id.textView_price_min);
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
                    textView_price_min.setText("RM "+String.valueOf(value));
                } else {
                    textView_price_max.setText("RM "+String.valueOf(value));
                }
            }
        });
        //END

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), IntroSettingThreeActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), IntroSettingActivity.class);
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
