package com.space.spaceapps.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.space.spaceapps.Common.MultipartUtility;
import com.space.spaceapps.Common.StandardProgressDialog;
import com.space.spaceapps.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class EditProfileActivity extends AppCompatActivity {
    String token;
    StandardProgressDialog standardProgressDialog;
    TextInputEditText et_fullname,et_username,et_email,et_phone;
    ImageView imageView_profile_image,imageView_camera,imageView_back;
    private Uri mCropImageUri;
    Button button_save;
    private Uri image_profile;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());
        token = getIntent().getStringExtra("token");
        et_fullname = findViewById(R.id.et_fullname);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        imageView_profile_image = findViewById(R.id.imageView_profile_image);
        imageView_camera = findViewById(R.id.imageView_camera);
        button_save = findViewById(R.id.button_save);
        imageView_back = findViewById(R.id.imageView_back);

        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    standardProgressDialog.show();
                    updateProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getDetails();
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
                                if(!dataObj.getString("username").equals("null")){
                                    et_username.setText(dataObj.getString("username"));
                                }

                                if(!dataObj.getString("email").equals("null")){
                                    et_email.setText(dataObj.getString("email"));
                                }

                                if(!dataObj.getString("contact_no").equals("null")){
                                    et_phone.setText(dataObj.getString("contact_no"));
                                }
                                if(!dataObj.getString("fullname").equals("null")){
                                    et_fullname.setText(dataObj.getString("fullname"));
                                }

                                String url_image = dataObj.getString("profile_picture").replace("35.238.24.65","104.154.35.121");
                                Picasso.get().load(url_image).into(imageView_profile_image);
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

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d("result",result.getUri().getPath());
                path = result.getUri().getPath();

                image_profile = result.getUri();
                imageView_profile_image.setImageURI(result.getUri());
                image_profile = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void updateProfile() throws IOException {
        Log.d("token",token);
        if(image_profile!=null){
            String charset = "UTF-8";
            String requestURL = "http://104.154.35.121/api/public/index.php/api/v1.0/update-profile";
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFormField("token", token);

            multipart.addFormField("fullname", et_fullname.getText().toString());
            multipart.addFormField("username", et_username.getText().toString());
            multipart.addFormField("email", et_email.getText().toString());
            multipart.addFormField("contact_no", et_phone.getText().toString());

            File imageFile = new File(path);
            multipart.addFilePart("profile_picture",imageFile);

            String response = String.valueOf(multipart.finish());
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i =0 ; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getString("status").equals("true")){
                        Log.d("response on",response);
                        standardProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Update Successfull",Toast.LENGTH_SHORT).show();
                        Intent next = new Intent(getApplicationContext(),DashboardActivity.class);
                        next.putExtra("token",token);
                        startActivity(next);
                    }else{
                        Log.d("response on",response);
                        standardProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            String charset = "UTF-8";
            String requestURL = "http://104.154.35.121/api/public/index.php/api/v1.0/update-profile";
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFormField("token", token);

            Log.d("EMAIL",et_email.getText().toString());
            multipart.addFormField("fullname", et_fullname.getText().toString());
            multipart.addFormField("username", et_username.getText().toString());
            multipart.addFormField("email", et_email.getText().toString());
            multipart.addFormField("contact_no", et_phone.getText().toString());

            String response = String.valueOf(multipart.finish());

            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i =0 ; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getString("status").equals("true")){
                        Log.d("response on",response);
                        standardProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Update Successfull",Toast.LENGTH_SHORT).show();
                        Intent next = new Intent(getApplicationContext(),DashboardActivity.class);
                        next.putExtra("token",token);
                        startActivity(next);
                    }else{
                        Log.d("response on",response);
                        standardProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();

        Log.d("URI",s);
        return s;
    }
}
