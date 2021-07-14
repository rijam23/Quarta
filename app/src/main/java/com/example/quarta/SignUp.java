package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    TextView signupbtn;
    EditText firstName,emailNum,passWord,lastName;
    Button createAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailNum = findViewById(R.id.inputmobile);
        passWord = findViewById(R.id.inputPassword);

        signupbtn = findViewById(R.id.signinbuttonlogin);





        final EditText inputmobile = findViewById(R.id.inputmobile);
        Button buttonOTP = findViewById(R.id.signupbutton);

        final ProgressBar progressBar = findViewById(R.id.progressbar);



        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                try {
                    postRequest(emailNum.getText().toString(),passWord.getText().toString(),firstName.getText().toString(),lastName.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputmobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonOTP.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+63" + inputmobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        SignUp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonOTP.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonOTP.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), Otp.class);
                                intent.putExtra("mobile",inputmobile.getText().toString());
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }
                        }
                );



            }
        });
        

    }
    public void postRequest(String signInEmailNum, String signInPassword, String firstname, String lastname) throws IOException {
        //Toast.makeText(MainActivity.this, signInEmailNum+signInPassword, Toast.LENGTH_SHORT).show();
        String url = "https://script.google.com/macros/s/AKfycbzmr1CpikywdCBGwiUOzMu-xG3CN0JE0nlBYo-n7AvXLFaHwM0B-5SaLLFE9EGBhafO/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action","register")
                .addFormDataPart("email", signInEmailNum)
                .addFormDataPart("password", signInPassword)
                .addFormDataPart("firstname", firstname)
                .addFormDataPart("lastname",lastname)
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
                        if(responseText.equals("Success")){
                            Toast.makeText(SignUp.this, responseText, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            //storing
                        }
                        else{
                            Toast.makeText(SignUp.this, responseText, Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e(TAG, mMessage);
            }
        });
    }

    ////Transparent Status Bar
}