package com.example.quarta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    Button signin_button,signin_button2;
    TextView textsignup;
    EditText emailNum;
    EditText password;
    ConstraintLayout loadingScr;
    ConstraintLayout v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        v = findViewById(R.id.constraintMain);
        loadingScr = findViewById(R.id.loading);
        loadingScr.setVisibility(View.GONE);
        signin_button = findViewById(R.id.signinbutton);
        signin_button2 = findViewById(R.id.signinbutton2);
        textsignup = findViewById(R.id.signupactbutton);
        emailNum = findViewById(R.id.loginmobiletext);
        password = findViewById(R.id.loginpasstext);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.login_anim);
        /*signin_button.startAnimation(animation);
        signin_button2.startAnimation(animation);
        textsignup.startAnimation(animation);
        emailNum.startAnimation(animation);
        password.startAnimation(animation);*/
        v.startAnimation(animation);

        SharedPreferences checkPref = getSharedPreferences("IsLogged",MODE_PRIVATE);

        String number = checkPref.getString("LoseNumber","");
        if (number.equals("88789")){
            ///
            // creating a variable for our BiometricManager
            // and lets check if our user can use biometric sensor or not
            SharedPreferences sf = getSharedPreferences("Client",MODE_PRIVATE);
            String cNUm = sf.getString("Contact_Number","");
            Toast.makeText(MainActivity.this, cNUm.substring(1), Toast.LENGTH_SHORT).show();
            emailNum.setText(cNUm.substring(1));

            BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    loadingScr.setVisibility(View.GONE);
                    //signin_button.setText("Log In with Fingerprint");
                    //msgtex.setText("You can use the fingerprint sensor to login");
                    //msgtex.setTextColor(Color.parseColor("#fafafa"));
                    break;

                // this means that the device doesn't have fingerprint sensor
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:

                    // this means that biometric sensor is not available
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    signin_button2.setVisibility(View.GONE);
                    loadingScr.setVisibility(View.GONE);
                    //msgtex.setText("This device doesnot have a fingerprint sensor");
                    //loginbutton.setVisibility(View.GONE);
                    break;

                //msgtex.setText("The biometric sensor is currently unavailable");
                    //loginbutton.setVisibility(View.GONE);

                // this means that the device doesn't contain your fingerprint
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    loadingScr.setVisibility(View.GONE);
                    signin_button2.setVisibility(View.GONE);
                    //msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                    //loginbutton.setVisibility(View.GONE);
                    break;
            }
            // creating a variable for our Executor
            Executor executor = ContextCompat.getMainExecutor(this);
            // this will give us result of AUTHENTICATION
            final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    loadingScr.setVisibility(View.GONE);
                }

                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    loadingScr.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(MainActivity.this, HomeDashBoard.class);
                    startActivity(intent2);
                    //loginbutton.setText("Login Successful");
                }
                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    loadingScr.setVisibility(View.GONE);
                }
            });
            // creating a variable for our promptInfo
            // BIOMETRIC DIALOG
            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("LogIn to Quarta Using Fingerprint")
                    .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();

            signin_button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingScr.setVisibility(View.VISIBLE);
                    biometricPrompt.authenticate(promptInfo);

                }
            });

            ///
            /*Intent intent2 = new Intent(MainActivity.this, HomeDashBoard.class);
            startActivity(intent2);*/
        }
        else{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            signin_button2.setVisibility(View.GONE);


            textsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                    startActivity(intent);
                }
            });


        }
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingScr.setVisibility(View.VISIBLE);
                PostData();
            }
        });

    }
    public void PostData(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String signInEmailNum = emailNum.getText().toString();
            String signInPassword = password.getText().toString();
            String url = "https://script.google.com/macros/s/AKfycby-EJdFayXO07cWGBIHukZx8xQSNgPbdJlJe3DCZIwHWBvfNQ0hCltw9InPACdB0-aX/exec";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "login")
                    .addFormDataPart("number", "0" + signInEmailNum)
                    .addFormDataPart("password", signInPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.w("failure Response", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseText = response.body().string();
                        if (responseText.equals("Errors")) {
                            loadingScr.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_SHORT).show();
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseText);
                                        JSONArray cast = jsonObject.getJSONArray("item");
                                        for (int i = 0; i < cast.length(); i++) {
                                            JSONObject actor = cast.getJSONObject(i);

                                            String First_Name = actor.getString("First Name");
                                            String Middle_Name = actor.getString("Middle Name");
                                            String Last_Name = actor.getString("Last Name");
                                            String Suffix = actor.getString("Suffix");
                                            String Date_of_Birth = actor.getString("Date of Birth");
                                            String Sex = actor.getString("Sex");
                                            String Contact_Number = actor.getString("Contact Number");
                                            String Cellular_Network = actor.getString("Cellular Network");
                                            String Address = actor.getString("Address");
                                            String Email_Address = actor.getString("Email Address");
                                            String Profile_Photo = actor.getString("Profile Photo");
                                            Toast.makeText(MainActivity.this, First_Name + Middle_Name + Last_Name + Suffix + Date_of_Birth, Toast.LENGTH_SHORT).show();
                                            SharedPreferences clientPref = getSharedPreferences("Client", MODE_PRIVATE);
                                            SharedPreferences.Editor editing = clientPref.edit();
                                            editing.putString("First_Name", First_Name);
                                            editing.putString("Middle_Name", Middle_Name);
                                            editing.putString("Last_Name", Last_Name);
                                            editing.putString("Suffix", Suffix);
                                            editing.putString("Date_of_Birth", Date_of_Birth);
                                            editing.putString("Sex", Sex);
                                            editing.putString("Contact_Number", Contact_Number);
                                            editing.putString("Cellular_Network", Cellular_Network);
                                            editing.putString("Address", Address);
                                            editing.putString("Email_Address", Email_Address);
                                            editing.putString("Profile_Photo", Profile_Photo);

                                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(Profile_Photo).getContent());
                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                            editing.putString("Base64_Photo", encoded);

                                            //Toast.makeText(MainActivity.this, "Thrededdddddddddd", Toast.LENGTH_SHORT).show();

                                            editing.apply();
                                            SharedPreferences LoggedPref = getSharedPreferences("IsLogged",MODE_PRIVATE);
                                            SharedPreferences.Editor LoggedEdit = LoggedPref.edit();
                                            LoggedEdit.putString("LoseNumber","88789");
                                            LoggedEdit.apply();
                                            loadingScr.setVisibility(View.GONE);
                                            Intent intent = new Intent(MainActivity.this, HomeDashBoard.class);
                                            startActivity(intent);


                                        }
                                    } catch (JSONException e) {

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }


                    } catch (NullPointerException e) {
                        Log.e("Error", String.valueOf(e));
                    }
                }
            });
        } catch (RuntimeException e) {
            Log.e("Error", String.valueOf(e));
        }

    }


    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}