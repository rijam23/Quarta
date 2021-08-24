package com.example.quarta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Verification_Activity extends AppCompatActivity {

    Uri urs = null;
    ImageView idImage;
    Button uploadPic;
    String encImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        idImage = findViewById(R.id.verifyId);
        uploadPic = findViewById(R.id.uploadVerifyBtn);

        idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                someActivityResultLaunchers.launch(intent);
                Toast.makeText(Verification_Activity.this, "", Toast.LENGTH_SHORT).show();

            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String clientId = sharedPreferences.getString("clientID","");
                SharedPreferences clientPref = getSharedPreferences("Client", MODE_PRIVATE);
                String number = clientPref.getString("Contact_Number","");

                if (urs != null) {
                    InputStream imageStream = null;

                    try {
                        imageStream = getContentResolver().openInputStream(urs);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] bytes = baos.toByteArray();
                    encImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                } else {
                    Toast.makeText(Verification_Activity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://script.google.com/macros/s/AKfycbx67VGAjlsj5nlQ0d9NqufpYZzn36zgbHuWK80TMbNaPNO7-ktgnrMf3savEWvNfTN8/exec";

                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action","verification")
                        .addFormDataPart("clientID", clientId)
                        .addFormDataPart("contactNumber", "'"+number)

                        .addFormDataPart("validID",encImage)

                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //String mMessage = e.getMessage().toString();
                        //Log.w("failure Response", mMessage);
                        //call.cancel();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(responseText.equals("Success")){
                                    Intent intent = new Intent(Verification_Activity.this,HomeDashBoard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    //storing

                                }
                                else{
                                    Toast.makeText(Verification_Activity.this, responseText, Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Log.e(TAG, mMessage);
                    }
                });




            }
        });



    }

    ActivityResultLauncher<Intent> someActivityResultLaunchers = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        urs = data.getData();
                        idImage.setImageURI(urs);


                    }
                }
            });
}