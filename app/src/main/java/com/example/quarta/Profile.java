package com.example.quarta;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


import de.hdodenhof.circleimageview.CircleImageView;
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
    CircleImageView clientImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        fullNameTv = findViewById(R.id.fullname);
        numberTv = findViewById(R.id.contactNumber);
        SharedPreferences getShared = getSharedPreferences("Client", MODE_PRIVATE);
        fullNameTv.setText(getShared.getString("First_Name","")+" "+getShared.getString("Middle_Name","")+" "+getShared.getString("Last_Name",""));
        clientImage = findViewById(R.id.profile_image);
        fullNameTv.setTextSize(20);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(getShared.getString("Profile_Photo","")).getContent());
            clientImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        numberTv.setText(getShared.getString("Contact_Number",""));

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profileicon);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
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


}