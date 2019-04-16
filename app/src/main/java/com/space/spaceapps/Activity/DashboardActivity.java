package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.Login;
import com.space.spaceapps.Activity.IntroSetting.IntroSettingActivity;
import com.space.spaceapps.Common.StandardProgressDialog;
import com.space.spaceapps.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout linear_edit_profile,linear_logout;
    String token = "";
    StandardProgressDialog standardProgressDialog;
    TextView textView_name;
    ImageView imageView_profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());
        linear_edit_profile = findViewById(R.id.linear_edit_profile);
        linear_logout = findViewById(R.id.linear_logout);
        token = getIntent().getStringExtra("token");
        textView_name = findViewById(R.id.textView_name);
        imageView_profile_image = findViewById(R.id.imageView_profile_image);

        standardProgressDialog.show();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        getDetails();
        linear_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),EditProfileActivity.class);
                next.putExtra("token",token);
                startActivity(next);
            }
        });

        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(next);
            }
        });
    }

    private void getDetails() {
        StringRequest stringRequest = new StringRequest(GET, "http://104.154.35.121/api/public/index.php/api/v1.0/me?token="+token,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("true")){
                                JSONObject dataObj = new JSONObject(jsonObject.getString("data"));
                                textView_name.setText("Welcome, "+dataObj.getString("username"));

                                if(!dataObj.getString("profile_picture").equals("null")){
                                    String url_image = dataObj.getString("profile_picture").replace("35.238.24.65","104.154.35.121");
                                    Picasso.get().load(url_image).into(imageView_profile_image);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.toString());
                        standardProgressDialog.dismiss();
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
