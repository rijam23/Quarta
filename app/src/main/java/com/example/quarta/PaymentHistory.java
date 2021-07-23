package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Typeface;
import android.icu.text.Edits;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentHistory extends AppCompatActivity {
    LinearLayout newLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        newLayout = findViewById(R.id.linearLayoutPayment);

        String url = "https://script.google.com/macros/s/AKfycby59IA7QPs4QqIz47X79eInl5TI5zMCnm-MD4bUJI3naP6bcD1p_UU10XB9EOiVu7kw/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "getLoanBreakdowns")
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
                                String lastPayment="";
                                String statut="";
                                String status = actor.getString("loanterm");
                                Toast.makeText(PaymentHistory.this, status, Toast.LENGTH_SHORT).show();


                                String amount = actor.getString("loanid");


                                //Toast.makeText(PaymentHistory.this, newJSonArr, Toast.LENGTH_SHORT).show();
                                //JSONObject newJsonOb = new JSONObject(newJSonArr);


                                LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);

                                LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);

                                LinearLayout.LayoutParams matchWrap = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                matchWrap.setMargins(20, 10, 20, 10);

                                CardView cardview = new CardView(getApplicationContext());
                                cardview.setLayoutParams(matchWrap);
                                cardview.setRadius(10);
                                cardview.setCardBackgroundColor(getResources().getColor(R.color.white));

                                LinearLayout newLinear1 = new LinearLayout(getApplicationContext());
                                newLinear1.setOrientation(LinearLayout.VERTICAL);
                                newLinear1.setLayoutParams(wrap);

                                LinearLayout newLinear2 = new LinearLayout(getApplicationContext());
                                newLinear2.setOrientation(LinearLayout.HORIZONTAL);
                                newLinear2.setLayoutParams(wrap);

                                LinearLayout newLinear3 = new LinearLayout(getApplicationContext());
                                newLinear3.setOrientation(LinearLayout.HORIZONTAL);
                                newLinear3.setLayoutParams(wrap);

                                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                                newLinear.setOrientation(LinearLayout.VERTICAL);
                                newLinear.setGravity(Gravity.START);

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
                                tvStatus1.setText("         Loan Term:                    ");
                                tvStatus1.setTextSize(20);

                                TextView tvAmount = new TextView(getApplicationContext());
                                tvAmount.setText(amount);
                                tvAmount.setTextColor(getResources().getColor(R.color.green));
                                tvAmount.setTextSize(20);
                                tvAmount.setTypeface(tF);
                                TextView tvAmount1 = new TextView(getApplicationContext());
                                tvAmount1.setText("         Loan ID:       ");
                                tvAmount1.setTextSize(20);


                                TextView tvPayment1 = new TextView(getApplicationContext());
                                tvPayment1.setText("        Dates:  ");
                                tvPayment1.setTextSize(10);
                                newLinear1.addView(tvPayment1);

                                JSONArray cast2 = actor.getJSONArray("monthlyBreakdown");
                                JSONArray cast3 = actor.getJSONArray("statusBreadkown");
                                for (int j = 0; j < cast2.length(); j++) {
                                    lastPayment = cast2.getString(j)+" ["+cast3.getString(j)+"]";
                                    TextView tvPayment = new TextView(getApplicationContext());

                                    tvPayment.setText(lastPayment);
                                    tvPayment.setTextColor(getResources().getColor(R.color.green));
                                    tvPayment.setTextSize(10);
                                    tvPayment.setTypeface(tF);


                                    newLinear1.addView(tvPayment);
                                }

                                for (int j = 0; j < cast2.length(); j++) {
                                    status = cast2.getString(j);
                                }



                                newLinear1.setGravity(Gravity.START);
                                newLinear2.addView(tvAmount1);
                                newLinear2.addView(tvAmount);
                                newLinear2.setGravity(Gravity.START);
                                newLinear3.addView(tvStatus1);
                                newLinear3.addView(tvStatus);
                                newLinear3.setGravity(Gravity.START);

                                newLinear.addView(newLinear1);
                                newLinear.addView(newLinear2);
                                newLinear.addView(newLinear3);

                                cardview.addView(newLinear);



                                newLayout.addView(cardview);



                            }

                        } catch (JSONException e) {
                            Toast.makeText(PaymentHistory.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error",e.toString());
                        }

                    }
                });

            }
        });
    }
}