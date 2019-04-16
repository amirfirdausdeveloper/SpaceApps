package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.space.spaceapps.Activity.IntroSetting.IntroSettingActivity;
import com.space.spaceapps.Common.StandardProgressDialog;
import com.space.spaceapps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {

    private static long back_pressed;
    TextView textView_createAccsa;
    Button button_login;
    TextInputEditText et_username,et_password;
    StandardProgressDialog standardProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        standardProgressDialog =  new StandardProgressDialog(this.getWindow().getContext());

        textView_createAccsa = findViewById(R.id.textView_createAccsa);
        button_login = findViewById(R.id.button_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.ey_password);

        textView_createAccsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(next);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardProgressDialog.show();
                if(et_username.getText().toString().equals("")){
                    et_username.setError("Please insert the value");
                    standardProgressDialog.dismiss();
                }else if(et_password.getText().toString().equals("")){
                    et_password.setError("Please insert the value");
                    standardProgressDialog.dismiss();
                }else{
                    standardProgressDialog.show();
                    login();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())  moveTaskToBack(true);
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    public void login(){
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
                                    Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                                    Intent next = new Intent(getApplicationContext(), IntroSettingActivity.class);
                                    next.putExtra("token",token.getString("token"));
                                    startActivity(next);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();
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
                params.put("password", et_password.getText().toString());
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
