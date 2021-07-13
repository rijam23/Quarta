package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(Profile.this,HomeDashBoard.class));
        this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}