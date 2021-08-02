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
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
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

    TextView logoutTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logoutTxt = findViewById(R.id.logoutTxtViewBtn);

        StrictMode.ThreadPolicy gfgPolicy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        fullNameTv = findViewById(R.id.fullname);
        numberTv = findViewById(R.id.contactNumber);
        SharedPreferences getShared = getSharedPreferences("Client", MODE_PRIVATE);
        fullNameTv.setText(getShared.getString("First_Name","")+" "+getShared.getString("Middle_Name","")+" "+getShared.getString("Last_Name",""));
        clientImage = findViewById(R.id.profile_image);
        fullNameTv.setTextSize(20);

        try {
            byte[] decodedString = Base64.decode(getShared.getString("Base64_Photo",""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            clientImage.setImageBitmap(decodedByte);
        } catch (Exception e) {
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

        clientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences logOutPref2 = getSharedPreferences("IsLogged",MODE_PRIVATE);
                SharedPreferences.Editor editDelete2 = logOutPref2.edit();
                editDelete2.clear();
                editDelete2.apply();

                SharedPreferences logOutPref = getSharedPreferences("Client",MODE_PRIVATE);
                SharedPreferences.Editor editDelete = logOutPref.edit();
                /*editDelete.putString("First_Name", "");
                editDelete.putString("Middle_Name", "");
                editDelete.putString("Last_Name", "");
                editDelete.putString("Suffix", "");
                editDelete.putString("Date_of_Birth", "");
                editDelete.putString("Sex", "");
                editDelete.putString("Contact_Number", "");
                editDelete.putString("Cellular_Network", "");
                editDelete.putString("Address", "");
                editDelete.putString("Email_Address", "");
                editDelete.putString("Profile_Photo", "");
                editDelete.putString("Base64_Photo", "");
                editDelete.apply();*/
                editDelete.clear();
                editDelete.apply();

                Toast.makeText(Profile.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

    }



}