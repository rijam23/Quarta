package com.example.quarta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity  {

    Button signin_button;
    TextView textsignup;
    EditText emailNum;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin_button = findViewById(R.id.signinbutton);
        textsignup = findViewById(R.id.signupactbutton);
        emailNum = findViewById(R.id.loginmobiletext);
        password = findViewById(R.id.loginpasstext);
        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String signInEmailNum = emailNum.getText().toString();
                    String signInPassword = password.getText().toString();
                    //postRequest(emailNum.getText().toString(),password.getText().toString());
                    String url = "https://script.google.com/macros/s/AKfycbyiRYBEIDmbG2cf1T_BKXHNGiOYpzZcMry43JZbEzRFm9whq72GCWj4dRl0o6vyki3y/exec";

                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action","login")
                            .addFormDataPart("email", signInEmailNum)
                            .addFormDataPart("password", signInPassword)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.w("failure Response", e);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try{
                            String responseText = response.body().string();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (responseText.equals("Login Success")) {
                                        Toast.makeText(MainActivity.this, responseText, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeDashBoard.class);
                                        startActivity(intent);
                                        //storing
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("email", signInEmailNum);
                                        myEdit.apply();
                                    } else {
                                        Toast.makeText(MainActivity.this, responseText, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        catch(NullPointerException e) {
                            Log.e("Error",String.valueOf(e));
                        }

                        }
                    });
                } catch(RuntimeException e) {
                    Log.e("Error", String.valueOf(e));
                }
            }
        });
    }
    public void postRequest(String signInEmailNum, String signInPassword) throws IOException {


    }
}