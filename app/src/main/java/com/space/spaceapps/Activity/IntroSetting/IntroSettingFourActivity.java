package com.space.spaceapps.Activity.IntroSetting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.space.spaceapps.Activity.DashboardActivity;
import com.space.spaceapps.R;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreen;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreenLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.apptik.widget.MultiSlider;

import static com.android.volley.Request.Method.POST;

public class IntroSettingFourActivity extends AppCompatActivity {

    TextView textView_sq_max,textView_sq_min;
    MultiSlider multiSlider;

    Button button_back,button_next;
    String token,postalCode,min,max,preferred_total_bedroom;
    String min_sq ="",max_sq;
    PreferenceManagerSplashScreenLogin preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_setting_four);

        preferenceManager = new PreferenceManagerSplashScreenLogin(this);
        token = getIntent().getStringExtra("token");
        postalCode = getIntent().getStringExtra("postalCode");
        min = getIntent().getStringExtra("min");
        max = getIntent().getStringExtra("max");
        preferred_total_bedroom = getIntent().getStringExtra("preferred_total_bedroom");

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
                    min_sq = String.valueOf(value);
                } else {
                    textView_sq_max.setText(String.valueOf(value)+" sq.ft");
                    max_sq = String.valueOf(value);
                }
            }
        });
        //END

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager.setIsFirstTimeLaunch(false);
                insertData();
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
                next.putExtra("postalCode",postalCode);
                next.putExtra("min",min);
                next.putExtra("max",max);

                startActivity(next);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Finish this setting first",Toast.LENGTH_SHORT).show();
    }

    public void insertData(){
        StringRequest stringRequest = new StringRequest(POST, "http://104.154.35.121/api/public/index.php/api/v1.0/update-general-settings",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("user_location", postalCode);
                params.put("monthly_commitment_min", min);
                params.put("monthly_commitment_max", max);
                params.put("preferred_total_bedroom",preferred_total_bedroom);

                params.put("preferred_total_squarefeet",max_sq);

                Log.d("TOSTRING",params.toString());
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
