package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanApplication extends AppCompatActivity {
    EditText clientId, loanId, brokerCode, fullName, loanAmount, purpose, source, broker, monthlyNI, end, monthIncluded;
    Button send;
    /*ImageView idImage;
    AnimationDrawable wifiannim;*/

    //Uri urs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);


        clientId = findViewById(R.id.clientId);
        loanId = findViewById(R.id.loanId);
        brokerCode = findViewById(R.id.broker);
        fullName = findViewById(R.id.fullName);
        loanAmount = findViewById(R.id.loanAmount);
        purpose = findViewById(R.id.purpose);
        source = findViewById(R.id.source);
        broker = findViewById(R.id.broker);
        monthlyNI = findViewById(R.id.monthlyNI);
        end = findViewById(R.id.end);
        monthIncluded = findViewById(R.id.monthIncluded);

        send = findViewById(R.id.submitBtn);

        /*idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //Intent intent = new Intent(uploadImageTest.this, pickpick.class);
                someActivityResultLaunchers.launch(intent);
            }
        });*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q;
                a = clientId.getText().toString();
                b = loanId.getText().toString();
                c = brokerCode.getText().toString();
                d = fullName.getText().toString();
                e = loanAmount.getText().toString();
                f = purpose.getText().toString();
                g = source.getText().toString();
                h = broker.getText().toString();
                i = monthlyNI.getText().toString();
                j = end.getText().toString();
                k = monthIncluded.getText().toString();



                    try {

                        String url = "https://script.google.com/macros/s/AKfycbz_BrXgpMwDni4A27zh-1Ji4eqzJ3gBu-3J83FyJHlUGVLeD_Yy8xTBZHktA13PS_LX/exec";
                        OkHttpClient client = new OkHttpClient();
                        Toast.makeText(LoanApplication.this, e, Toast.LENGTH_SHORT).show();
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("action", "addLoan")
                                .addFormDataPart("clienti" +
                                        "d", a)
                                .addFormDataPart("loanId", b)
                                .addFormDataPart("brokerCode", c)
                                .addFormDataPart("fullName", d)
                                .addFormDataPart("loanAmount", e)
                                .addFormDataPart("purpose", f)
                                .addFormDataPart("source", g)
                                .addFormDataPart("broker", h)
                                .addFormDataPart("monthlyNI", i)
                                .addFormDataPart("end", j)
                                .addFormDataPart("monthIncluded",k)
                                .build();
                        Request request = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //String mMessage = e.getMessage().toString();
                                //Log.w("failure Response", mMessage);
                                //call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseText = response.body().string();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (responseText.equals("Loan Success")) {
                                            Toast.makeText(LoanApplication.this, responseText, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), HomeDashBoard.class);
                                            startActivity(intent);
                                            //storing
                                        } else {
                                            Toast.makeText(LoanApplication.this, responseText, Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Log.e(TAG, mMessage);
                            }
                        });
                    } catch (NullPointerException e1) {

                    }

            }
        });
    }

    /*ActivityResultLauncher<Intent> someActivityResultLaunchers = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        urs = data.getData();

                        idImage.setImageURI(urs);
                    }
                }
            });*/


}
