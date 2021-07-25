package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity {
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(Profile.this,HomeDashBoard.class));
        this.finish();
    }

    TextView fullNameTv;
    TextView numberTv;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullNameTv = findViewById(R.id.fullname);
        numberTv = findViewById(R.id.contactNumber);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        number = sh.getString("number", "");

        getClientDetails(number,fullNameTv,"Full Name");
        getClientDetails(number,numberTv,"Contact Number");

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profileicon);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeicon:
                        startActivity(new Intent(getApplicationContext(),HomeDashBoard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.moneytalksicon:
                        startActivity(new Intent(getApplicationContext(),QuarTalk.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void getClientDetails(String number,TextView tv,String data){
        final String[] toReturn = {""};
        String url = "https://script.google.com/macros/s/AKfycbzTVgPgRkQl-ys4hT-pD0uYgpATgJhKqGXp5gyYkJCneu9HqjKOj6Bs91q_EXyS4aU/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action","fetchData")
                .addFormDataPart("number",number)
                .addFormDataPart("data",data)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String txtResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(txtResponse);
                    }
                });


            }
        });

    };
}