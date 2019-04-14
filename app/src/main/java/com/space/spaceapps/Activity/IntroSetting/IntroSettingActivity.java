package com.space.spaceapps.Activity.IntroSetting;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.space.spaceapps.Activity.DashboardActivity;
import com.space.spaceapps.Activity.IntroScreenActivity;
import com.space.spaceapps.Activity.LoginActivity;
import com.space.spaceapps.R;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreen;
import com.space.spaceapps.Utils.PreferenceManagerSplashScreenLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class IntroSettingActivity extends AppCompatActivity {
    String token = "";
    PreferenceManagerSplashScreenLogin preferenceManager;
    Button button_next;
    EditText editText_address;
    String postalCode;
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
        editText_address = findViewById(R.id.editText_address);

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
                if(editText_address.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please insert city",Toast.LENGTH_SHORT).show();
                }else{
                    convertAddress(editText_address.getText().toString());
                }


            }
        });
    }

    public void convertAddress(String address) {
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (address != null && !address.isEmpty()) {
            try {
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();

                    Log.d("lat", String.valueOf(lat));
                    Log.d("lng", String.valueOf(lng));

                    String message=String.format("City Name %s \n PostalCode %d \n",  addressList.get(0).getLocality(),addressList.get(0).getPostalCode());
                    Log.d("message",message);

                    getLocationAddress(lat,lng);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
    } // end convertAddress

    private void getLocationAddress(double lat, double lng) {
        StringRequest stringRequest = new StringRequest(GET, "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&key=AIzaSyBgAkAAaBvSu1SHQdd3EidTgzWXCF1yZbw",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseOBJ = new JSONObject(response);
                            JSONArray resultsOBJ = new JSONArray(responseOBJ.getString("results"));
                            for (int i =0; i <resultsOBJ.length(); i++){
                                JSONObject resultOB = resultsOBJ.optJSONObject(i);

                                JSONArray addRESSOBJ = new JSONArray(resultOB.getString("address_components"));
                                for(int ii = 0; ii <addRESSOBJ.length(); ii++){
                                    JSONObject lolol = addRESSOBJ.optJSONObject(ii);
                                    if(lolol.getString("types").equals("[\"postal_code\"]")){
                                        postalCode = lolol.getString("long_name");
                                    }
                                }
                            }
                            Intent next = new Intent(getApplicationContext(),IntroSettingTwoActivity.class);
                            next.putExtra("token",token);
                            next.putExtra("postalCode",postalCode);
                            startActivity(next);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
