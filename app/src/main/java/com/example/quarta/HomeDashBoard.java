package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeDashBoard extends AppCompatActivity {

    private long pressedTime;
    Button applyLoan;
    ImageView loanHistory;

    @Override
    public void onBackPressed()
    {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dash_board);
        loanHistory = findViewById(R.id.loanHistory);
        loanHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeDashBoard.this,activeLoans.class);
                startActivity(intent);
            }
        });
        applyLoan = findViewById(R.id.applybutton);
        applyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeDashBoard.this, LoanApplication.class);
                startActivity(intent);
            }
        });

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.homeicon);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profileicon:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
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
////Transparent Status Bar
    private static void setWindowsFlag(Activity activity, final  int Bits, Boolean on)
    {
        Window win = activity.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on){
            Winparams.flags |=Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);
    }
    ////Transparent Status Bar
}