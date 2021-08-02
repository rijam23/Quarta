package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfile extends AppCompatActivity {
    EditText dateofbirthh,edFirstname,edMiddlename,edLastname,edAddress,edPassword;
    ImageView applyChanges;
    String emailAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        applyChanges = findViewById(R.id.applyChanges);
        dateofbirthh = findViewById(R.id.dateofbirthEdit);
        edFirstname = findViewById(R.id.txtEditFirstname);
        edMiddlename = findViewById(R.id.txtEditMiddleName);
        edLastname = findViewById(R.id.txtEditLastname);
        edAddress = findViewById(R.id.txtEditAddress);

        SharedPreferences sharedPreferences = getSharedPreferences("Client",MODE_PRIVATE);

        emailAdd = sharedPreferences.getString("Email_Address","");

        edFirstname.setText(sharedPreferences.getString("First_Name",""));
        edLastname.setText(sharedPreferences.getString("Last_Name",""));
        edMiddlename.setText(sharedPreferences.getString("Middle_Name",""));
        edAddress.setText(sharedPreferences.getString("Address",""));
        dateofbirthh.setText(sharedPreferences.getString("Date_of_Birth",""));





        AutoCompleteTextView surfix1 = (AutoCompleteTextView) findViewById(R.id.surfixEdit);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, surfix);
        surfix1.setAdapter(adapter1);


        surfix1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfix1.showDropDown();
            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        dateofbirthh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = month + "/" + day + "/" + year;
                        dateofbirthh.setText(date);
                        Toast.makeText(EditProfile.this, dateofbirthh.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editData(emailAdd,"First Name",edFirstname.getText().toString());
                editData(emailAdd,"Middle Name",edLastname.getText().toString());
                editData(emailAdd,"Last Name",edLastname.getText().toString());
                editData(emailAdd,"Suffix",surfix1.getText().toString());
                editData(emailAdd,"Address",edAddress.getText().toString());
                editData(emailAdd,"Date of Birth",dateofbirthh.getText().toString());
                SharedPreferences logOutPref2 = getSharedPreferences("IsLogged",MODE_PRIVATE);
                SharedPreferences.Editor editDelete2 = logOutPref2.edit();
                editDelete2.clear();
                editDelete2.apply();

                SharedPreferences logOutPref = getSharedPreferences("Client",MODE_PRIVATE);
                SharedPreferences.Editor editDelete = logOutPref.edit();
                /*editDelete.putString("First_Name", "");
                editDelete.putString("Middle_Name", "");
                editDelete.putString("Last_Name", "");
                editDelete.putString("Suffix", "");
                editDelete.putString("Date_of_Birth", "");
                editDelete.putString("Sex", "");
                editDelete.putString("Contact_Number", "");
                editDelete.putString("Cellular_Network", "");
                editDelete.putString("Address", "");
                editDelete.putString("Email_Address", "");
                editDelete.putString("Profile_Photo", "");
                editDelete.putString("Base64_Photo", "");
                editDelete.apply();*/
                editDelete.clear();
                editDelete.apply();

                Toast.makeText(EditProfile.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditProfile.this, "Please LogIn Again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);


            }
        });

    }

    private static final String[] surfix = new String[]{"Jr", "Sr", "III", "N/A"};

    public void editData(String email,String data, String updateData){
        String url = "https://script.google.com/macros/s/AKfycbyQDLrKQ284xQ9d32vE2DVXY2PlrGkXSalV4qvP34K2KZdo8w6dGTwS6o9Ms6n7wp_f/exec";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "editData")
                .addFormDataPart("email", email)
                .addFormDataPart("data", data)
                .addFormDataPart("updatedDATA", updateData)
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

            }
        });













    }
}