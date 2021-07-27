package com.example.quarta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    Button signin_button;
    TextView textsignup;
    EditText emailNum;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin_button = findViewById(R.id.signinbutton);
        textsignup = findViewById(R.id.signupactbutton);
        emailNum = findViewById(R.id.loginmobiletext);
        password = findViewById(R.id.loginpasstext);


        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String signInEmailNum = emailNum.getText().toString();
                    String signInPassword = password.getText().toString();
                    //postRequest(emailNum.getText().toString(),password.getText().toString());
                    String url = "https://script.google.com/macros/s/AKfycbz17cvcFuontel7Bzxn9rorUBqOeKl_8AaIIv1E79OdhwMxiHt_RcXpLqCYJAnm87Ku/exec";

                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "login")
                            .addFormDataPart("number", "0"+signInEmailNum)
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
                                if(responseText.equals("Errors")){

                                }
                                else{
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
                                                    Toast.makeText(MainActivity.this, First_Name+Middle_Name+Last_Name+Suffix+Date_of_Birth, Toast.LENGTH_SHORT).show();
                                                    SharedPreferences clientPref = getSharedPreferences("Client",MODE_PRIVATE);
                                                    SharedPreferences.Editor editing = clientPref.edit();
                                                    editing.putString("First_Name",First_Name );
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
                                                    editing.apply();
                                                    Intent intent = new Intent(MainActivity.this,HomeDashBoard.class);
                                                    startActivity(intent);


                                                }
                                            }catch (JSONException e){

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