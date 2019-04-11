package com.space.spaceapps.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegistrationSecondActivity extends AppCompatActivity {

    String account_created_from = "",image_url="";
    TextInputEditText et_email,et_username,et_password;
    Button button_register;
    StandardProgressDialog standardProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_second);

        standardProgressDialog =  new StandardProgressDialog(this.getWindow().getContext());

        et_email = findViewById(R.id.et_email);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.ey_password);
        button_register = findViewById(R.id.button_register);

        if(getIntent().getStringExtra("account_created_from").equals("2")){
            et_email.setText(getIntent().getStringExtra("email"));
            account_created_from = getIntent().getStringExtra("account_created_from");
            image_url = getIntent().getStringExtra("image_url");
        }else if(getIntent().getStringExtra("account_created_from").equals("3")){
            et_email.setText(getIntent().getStringExtra("email"));
            account_created_from = getIntent().getStringExtra("account_created_from");
            image_url = getIntent().getStringExtra("image_url");
        }else{
            account_created_from = "1";
            image_url = "null";
        }

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email.getText().toString().equals("")){
                    et_email.setError("Please fill the value");
                }else if(et_password.getText().toString().equals("")){
                    et_password.setError("Please fill the value");
                }else if(et_username.getText().toString().equals("")){
                    et_username.setError("Please fill the value");
                }else{
                    register();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivity(next);
    }

    public void register(){
        standardProgressDialog.show();
        StringRequest stringRequest = new StringRequest(POST, "http://104.154.35.121/api/public/index.php/api/v1.0/auth/register",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("status").equals("true")){
                                autoLogin();
                            }else{

                                standardProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),object.getString("errors"),Toast.LENGTH_SHORT).show();
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
                params.put("username", et_username.getText().toString());
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());
                params.put("account_created_from",account_created_from);
                params.put("image_url",image_url);
                Log.d("param",params.toString());
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void autoLogin(){
        StringRequest stringRequest = new StringRequest(POST, "http://104.154.35.121/api/public/index.php/api/v1.0/auth/login",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("status").equals("true")){
                                if(object.has("data")){
                                    JSONObject token = new JSONObject(object.getString("data"));

                                    Toast.makeText(getApplicationContext(),"Registration successful",Toast.LENGTH_SHORT).show();
                                    Intent next = new Intent(getApplicationContext(), IntroSettingActivity.class);
                                    next.putExtra("token",token.getString("token"));
                                    startActivity(next);
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
                params.put("username", et_username.getText().toString());
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());
                params.put("account_created_from",account_created_from);
                params.put("image_url",image_url);
                Log.d("param",params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
