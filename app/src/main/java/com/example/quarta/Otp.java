package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Otp extends AppCompatActivity {
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String verificationnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        SharedPreferences myPref = getSharedPreferences("ImageBase64",MODE_PRIVATE);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        String middlename = getIntent().getStringExtra("middlename");
        String suffix = getIntent().getStringExtra("suffix");
        String sex = getIntent().getStringExtra("sex");
        String dateOfBirth = getIntent().getStringExtra("dateOfBirth");
        String number = getIntent().getStringExtra("mobile");
        String Address = getIntent().getStringExtra("address");
        String network = getIntent().getStringExtra("networkProvider");
        String base64Image = myPref.getString("Base64Image","");



        inputCode1 = findViewById(R.id.inputcode1);
        inputCode2 = findViewById(R.id.inputcode2);
        inputCode3 = findViewById(R.id.inputcode3);
        inputCode4 = findViewById(R.id.inputcode4);
        inputCode5 = findViewById(R.id.inputcode5);
        inputCode6 = findViewById(R.id.inputcode6);

        setupOtpInputs();
        final ProgressBar progressBar = findViewById(R.id.progressbar1);
        final Button buttonVerify = findViewById(R.id.verifybutton);


        verificationnId = getIntent().getStringExtra("verificationId");

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().isEmpty()
                || inputCode2.getText().toString().isEmpty()
                || inputCode3.getText().toString().isEmpty()
                || inputCode4.getText().toString().isEmpty()
                || inputCode5.getText().toString().isEmpty()
                || inputCode6.getText().toString().isEmpty()) {
                    Toast.makeText(Otp.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code =
                        inputCode1.getText().toString() +
                                inputCode2.getText().toString() +
                                inputCode3.getText().toString() +
                                inputCode4.getText().toString() +
                                inputCode5.getText().toString() +
                                inputCode6.getText().toString();

                if (verificationnId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationnId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonVerify.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        try {
                                            postRequest(email,password,firstname,lastname,middlename,suffix,sex,dateOfBirth,number,Address,network,base64Image);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(Otp.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                        
                                    }


                                }
                            });
                    findViewById(R.id.resendcodebutton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+63" + getIntent().getStringExtra("mobile"),
                                    60,
                                    TimeUnit.SECONDS,
                                    Otp.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                            Toast.makeText(Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            verificationnId = newVerificationId;
                                            Toast.makeText(Otp.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );

                        }
                    });
                }

            }
        });



    }

    private void  setupOtpInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Please enter the OTP to continue",
                    Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
    }



    public void postRequest(String signInEmailNum,
                            String signInPassword,
                            String firstname,
                            String lastname,String middlename,
                            String suffix,
                            String sex,
                            String dateOfBirth,
                            String number,
                            String Address,
                            String network,
                            String base64Image) throws IOException {
        //Toast.makeText(MainActivity.this, signInEmailNum+signInPassword, Toast.LENGTH_SHORT).show();
        String url = "https://script.google.com/macros/s/AKfycbxJH_ts5JKQmF6kXmolDkVwj90Ak_NFa0_nkqf7-5AGv6axDcvREkez123Vn73wLStM/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action","register")
                .addFormDataPart("emailAddress", signInEmailNum)
                .addFormDataPart("password", signInPassword)
                .addFormDataPart("firstName", firstname)
                .addFormDataPart("lastName",lastname)
                .addFormDataPart("middleName",middlename)
                .addFormDataPart("suffix", suffix)
                .addFormDataPart("sex", sex)
                .addFormDataPart("dateOfBirth", dateOfBirth)
                .addFormDataPart("contactNumber", "'0"+number)
                .addFormDataPart("address", Address)
                .addFormDataPart("cellularNetwork", network)
                .addFormDataPart("profilePhoto",base64Image)

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
                            Intent intent = new Intent(Otp.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //storing

                        }
                        else{
                            Toast.makeText(Otp.this, responseText, Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e(TAG, mMessage);
            }
        });
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}