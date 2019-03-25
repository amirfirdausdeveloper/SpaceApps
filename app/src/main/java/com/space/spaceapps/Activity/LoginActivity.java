package com.space.spaceapps.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.space.spaceapps.R;

public class LoginActivity extends AppCompatActivity {

    private static long back_pressed;
    TextView textView_createAcc;
    Button button_login;
    TextInputEditText et_username,et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView_createAcc = findViewById(R.id.textView_createAcc);
        button_login = findViewById(R.id.button_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.ey_password);

        textView_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(next);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_username.getText().toString().equals("")){
                    et_username.setError("Please insert the value");
                }else if(et_password.getText().toString().equals("")){
                    et_password.setError("Please insert the value");
                }else{

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
}
