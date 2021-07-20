package com.example.quarta;

//import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
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

                                ViewGroup.LayoutParams layoutPar = new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(20, 10, 20, 10);

                                CardView cardview = new CardView(getApplicationContext());
                                cardview.setLayoutParams(layoutParams);
                                cardview.setRadius(10);
                                cardview.setCardBackgroundColor(getResources().getColor(R.color.white));

                                LinearLayout newLinear1 = new LinearLayout(getApplicationContext());
                                newLinear1.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout newLinear2 = new LinearLayout(getApplicationContext());
                                newLinear2.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout newLinear3 = new LinearLayout(getApplicationContext());
                                newLinear3.setOrientation(LinearLayout.HORIZONTAL);

                                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                                newLinear.setOrientation(LinearLayout.VERTICAL);

                                Typeface tF = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    tF = getResources().getFont(R.font.poppinsbold);
                                }

                                TextView tvStatus = new TextView(getApplicationContext());
                                tvStatus.setText(status);
                                tvStatus.setTextColor(getResources().getColor(R.color.green));
                                tvStatus.setTextSize(20);
                                tvStatus.setTypeface(tF);
                                TextView tvStatus1 = new TextView(getApplicationContext());
                                tvStatus1.setText("Status");

                                TextView tvAmount = new TextView(getApplicationContext());
                                tvAmount.setText(amount);
                                tvAmount.setTextColor(getResources().getColor(R.color.green));
                                tvAmount.setTextSize(20);
                                tvAmount.setTypeface(tF);
                                TextView tvAmount1 = new TextView(getApplicationContext());
                                tvAmount1.setText("Loan Amount");

                                TextView tvPayment = new TextView(getApplicationContext());
                                tvPayment.setText(lastPayment);
                                tvPayment.setTextColor(getResources().getColor(R.color.green));
                                tvPayment.setTextSize(20);
                                tvPayment.setTypeface(tF);
                                TextView tvPayment1 = new TextView(getApplicationContext());
                                tvPayment1.setText("Date of Payment");
                                newLinear1.addView(tvPayment1);
                                newLinear1.addView(tvPayment);
                                newLinear1.setGravity(Gravity.CENTER);
                                newLinear2.addView(tvAmount1);
                                newLinear2.addView(tvAmount);
                                newLinear2.setGravity(Gravity.CENTER);
                                newLinear3.addView(tvStatus1);
                                newLinear3.addView(tvStatus);
                                newLinear3.setGravity(Gravity.CENTER);

                                newLinear.addView(newLinear1);
                                newLinear.addView(newLinear2);
                                newLinear.addView(newLinear3);

                                cardview.addView(newLinear);



                                newLayout.addView(cardview);



                            }

                        } catch (JSONException e) {

                        }

                    }
                });

            }
        });
    }
}