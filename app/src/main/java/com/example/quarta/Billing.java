package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Billing extends AppCompatActivity {
    TextView dateOfBilling, coveredDate,principal,interest,totalAmo,lateFeePerDay,daysLate,totalPayment,fullNameTv;
    ImageView clientImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        clientImage = findViewById(R.id.userImage);
        fullNameTv = findViewById(R.id.billName);
        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        SharedPreferences sf = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String clientID = sf.getString("clientID", "");
        SharedPreferences getShared = getSharedPreferences("Client", MODE_PRIVATE);
        String url = "https://script.google.com/macros/s/AKfycbwK_j-ze2C-FtHiBxdD-h_ejG22DnqwtjD6wIK5PQFzRUcVYDSn2sVPO8EB4sERx7i5/exec";
        dateOfBilling = findViewById(R.id.dateOfBilling);
        coveredDate = findViewById(R.id.coveredDate);
        principal = findViewById(R.id.principal);
        interest = findViewById(R.id.interest);
        totalAmo = findViewById(R.id.totalAmortization);
        lateFeePerDay = findViewById(R.id.lateFeePerDay);
        daysLate = findViewById(R.id.daysLate);
        totalPayment = findViewById(R.id.totalPayment);

        fullNameTv.setText(getShared.getString("First_Name","")+" "+getShared.getString("Middle_Name","")+" "+getShared.getString("Last_Name",""));

        try {
            byte[] decodedString = Base64.decode(getShared.getString("Base64_Photo",""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            clientImage.setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "loanBill")
                .addFormDataPart("clientId", clientID)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseText);
                            JSONArray cast = jsonObject.getJSONArray("items");
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                //String status = actor.getString("status");
                                dateOfBilling.setText(actor.getString("dateOfBilling"));
                                coveredDate.setText(actor.getString("coveredDate"));
                                principal.setText(actor.getString("principal"));
                                interest.setText(actor.getString("interest"));
                                totalAmo.setText(actor.getString("totalAmo"));
                                lateFeePerDay.setText(actor.getString("lateFeePerDay"));
                                if(Integer.parseInt(actor.getString("daysLate")) < 0){
                                    daysLate.setText("0");
                                }
                                else{
                                    daysLate.setText(actor.getString("daysLate"));
                                }
                                totalPayment.setText(actor.getString("totalPayment"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}