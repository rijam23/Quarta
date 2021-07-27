package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeDashBoard extends AppCompatActivity {

    private long pressedTime;
    Button applyLoan;
    ImageView paymentHistory;
    ImageView loanHistory;
    String number;
    String CLientID;
    TextView greetings;
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


        greetings = (TextView)findViewById(R.id.greet);

        Calendar kalendaryo = Calendar.getInstance();

        int james = kalendaryo.get(Calendar.HOUR_OF_DAY);

        if(james >= 0 && james < 12) {
            greetings.setText("Magandang Araw,");
        }
        else if(james >= 12 && james < 17) {
            greetings.setText("Magandang Hapon,");
        }
        else if(james >= 17 && james < 24) {
            greetings.setText("Magandang gabi,");
        }
        else{
            greetings.setText("Kamusta ka,");
        }







        SharedPreferences sh = getSharedPreferences("Client", MODE_PRIVATE);

        number = sh.getString("Contact_Number", "");

        Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
        getClientId(number);
        Toast.makeText(this, CLientID, Toast.LENGTH_SHORT).show();
        paymentHistory = findViewById(R.id.paymenthistoryIcon);
        paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeDashBoard.this, PaymentHistory.class);
                intent.putExtra("clientID",CLientID);
                startActivity(intent);
            }
        });


        loanHistory = findViewById(R.id.loanHistory);
        loanHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeDashBoard.this, ActiveLoans.class);
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




    public void getClientId(String number){
        final String[] toReturn = {""};
        String url = "https://script.google.com/macros/s/AKfycbzbZcIYHAsNQdjs3uyUTvNL1O6kamME2N474zQEiBSXXnpiiOUvNtuDAWlrYz18DENX/exec";
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action","fetchData")
                .addFormDataPart("number",number)
                .addFormDataPart("data","Client ID")
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
                CLientID = txtResponse;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeDashBoard.this, txtResponse, Toast.LENGTH_SHORT).show();
                    }
                });
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("clientID", CLientID);
                myEdit.apply();

            }
        });

    };
}

