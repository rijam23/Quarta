package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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

public class activeLoans extends AppCompatActivity {

    LinearLayout newLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_loans);

        newLayout = findViewById(R.id.utangslayout);

        Toast.makeText(activeLoans.this, "hello", Toast.LENGTH_SHORT).show();
        String url = "https://script.google.com/macros/s/AKfycbz0fzk5Es4iCMpCSr2MwYJP4A0qleT2FVWu63q73dj705m2lJgU6VpG1jA9qhEDBJyw/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "activeloans")
                .addFormDataPart("clientId", "1986-NorieNobelaManilag")
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
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseText);
                            JSONArray cast = jsonObject.getJSONArray("items");
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String status = actor.getString("status");
                                String amount = actor.getString("amount");
                                String lastPayment = actor.getString("lastPayment");

                                Toast.makeText(activeLoans.this, status, Toast.LENGTH_SHORT).show();
                                Toast.makeText(activeLoans.this, amount, Toast.LENGTH_SHORT).show();
                                Toast.makeText(activeLoans.this, lastPayment, Toast.LENGTH_SHORT).show();
                                TextView tvStatus = new TextView(getApplicationContext());
                                tvStatus.setText("Status: "+status);

                                TextView tvAmount = new TextView(getApplicationContext());
                                tvAmount.setText("Amount: "+amount);

                                TextView tvPayment = new TextView(getApplicationContext());
                                tvPayment.setText("Last Payment: "+lastPayment);

                                newLayout.addView(tvAmount);
                                newLayout.addView(tvStatus);
                                newLayout.addView(tvPayment);



                            }

                        } catch (JSONException e) {

                        }

                    }
                });

            }
        });
    }
}