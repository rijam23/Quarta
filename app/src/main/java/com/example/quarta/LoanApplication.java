package com.example.quarta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanApplication extends AppCompatActivity {
    EditText emailAdd, dataAccess, loanAmount, loanTerm, firstName, middleName, lastName,  dateOfBirth, sex, currentAddress, contactNumber,  fbName, purposeBorrow, sourceOfIncome, monthlyNetIncome, howKnowBank, brokerCode;
    Button send;
    AutoCompleteTextView cellularNetwork,suffix;
    ImageView idImage, buttonback;
    String encImage;
    RadioGroup rg;
    RadioButton rdBtn;

    RadioGroup rgDataAccess;
    RadioButton rdBtnDataAccess;

    //AnimationDrawable wifiannim;*/
    DatePickerDialog.OnDateSetListener setListener;

    String[] language ={"SMART","GLOBE","SUN","DITO","GOMO"};

    Uri urs = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        SharedPreferences getShared = getSharedPreferences("Client",MODE_PRIVATE);

        rg = findViewById(R.id.groupSex);

        rgDataAccess = findViewById(R.id.rgDataAccess);


        emailAdd = findViewById(R.id.emailAdd);

        loanAmount = findViewById(R.id.loanamount);
        loanTerm = findViewById(R.id.loanterm);
        firstName = findViewById(R.id.firstname);
        middleName = findViewById(R.id.middlename);
        lastName = findViewById(R.id.lastname);
        suffix = findViewById(R.id.suffix);
        dateOfBirth = findViewById(R.id.dateofbirth);

        currentAddress = findViewById(R.id.address);
        contactNumber = findViewById(R.id.contactnumber);
        cellularNetwork = findViewById(R.id.autoCompleteTextView1);
        fbName = findViewById(R.id.fbname);
        purposeBorrow = findViewById(R.id.purposeofborrowing);
        sourceOfIncome = findViewById(R.id.sourceoffund);
        monthlyNetIncome = findViewById(R.id.monthlyIncome);
        howKnowBank = findViewById(R.id.howClarenceBank);
        brokerCode = findViewById(R.id.brokerCode);
        buttonback = findViewById(R.id.applybackbutton);
        firstName.setText(getShared.getString("First_Name",""));
        middleName.setText(getShared.getString("Middle_Name",""));
        lastName.setText(getShared.getString("Last_Name",""));
        suffix.setText(getShared.getString("Suffix",""));
        dateOfBirth.setText(getShared.getString("Date_of_Birth",""));

        contactNumber.setText(getShared.getString("Contact_Number",""));
        cellularNetwork.setText(getShared.getString("Cellular_Network",""));
        currentAddress.setText(getShared.getString("Address",""));
        emailAdd.setText(getShared.getString("Email_Address",""));







        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,Cellularnet);
        cellularNetwork.setAdapter(adapter2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,surfix);
        suffix.setAdapter(adapter);

        cellularNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cellularNetwork.showDropDown();
            }
        });



        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeDashBoard.class);
                startActivity(intent);

            }
        });




        idImage = findViewById(R.id.idImage);


        send = findViewById(R.id.submitBtn);




        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LoanApplication.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = month+"/"+day+"/"+year;
                        dateOfBirth.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });


        idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //Intent intent = new Intent(uploadImageTest.this, pickpick.class);
                someActivityResultLaunchers.launch(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t;
                int ids = rg.getCheckedRadioButtonId();
                rdBtn = findViewById(ids);
                int optionsDataAccess = rgDataAccess.getCheckedRadioButtonId();
                rdBtnDataAccess = findViewById(optionsDataAccess);

                a = emailAdd.getText().toString();
                b = rdBtnDataAccess.getText().toString();
                c = loanAmount.getText().toString();
                d = loanTerm.getText().toString();
                e = firstName.getText().toString();
                f = middleName.getText().toString();
                g = lastName.getText().toString();
                h = suffix.getText().toString();
                i = dateOfBirth.getText().toString();
                j = rdBtn.getText().toString();
                k = currentAddress.getText().toString();
                l = contactNumber.getText().toString();
                m = cellularNetwork.getText().toString();
                n = fbName.getText().toString();
                o = purposeBorrow.getText().toString();
                p = sourceOfIncome.getText().toString();
                q = monthlyNetIncome.getText().toString();
                r = howKnowBank.getText().toString();
                s = brokerCode.getText().toString();


                if (urs != null) {
                    InputStream imageStream = null;

                    try {
                        imageStream = getContentResolver().openInputStream(urs);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] bytes = baos.toByteArray();
                    encImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                    try {

                        String url = "https://script.google.com/macros/s/AKfycbyYkQ2wtm2neBKkL-F4ESKxsxA8ibNs7yoiDJtQAnDeFXmP2L0pfHWnEav7bxe4w0Y-/exec";
                        OkHttpClient client = new OkHttpClient();
                        Toast.makeText(LoanApplication.this, e, Toast.LENGTH_SHORT).show();
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("action", "form")
                                .addFormDataPart("emailAdd", a)
                                .addFormDataPart("dataAccess", b)
                                .addFormDataPart("loanAmount", c)
                                .addFormDataPart("loanTerm", d)
                                .addFormDataPart("firstName", e)
                                .addFormDataPart("middleName", f)
                                .addFormDataPart("lastName", g)
                                .addFormDataPart("suffix", h)
                                .addFormDataPart("dateOfBirth", i)
                                .addFormDataPart("sex", j)
                                .addFormDataPart("currentAddress", k)
                                .addFormDataPart("contactNumber", "0"+l)
                                .addFormDataPart("cellularNetwork", m)
                                .addFormDataPart("fbName", n)
                                .addFormDataPart("purposeBorrow", o)
                                .addFormDataPart("sourceOfIncome", p)
                                .addFormDataPart("monthlyNetIncome", q)
                                .addFormDataPart("howKnowBank", r)
                                .addFormDataPart("brokerCode", s)
                                .addFormDataPart("idTypeImage", encImage)
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
                                            Toast.makeText(LoanApplication.this,"Error Comms", Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Log.e(TAG, mMessage);
                            }
                        });
                    } catch (NullPointerException e1) {
                        Toast.makeText(LoanApplication.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    //
                } else {
                    Toast.makeText(LoanApplication.this, "Error Missing Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private static final String[] Cellularnet = new String[]{"SMART/TNT/SUN","GLOBE/TM/GOMO","DITO"};
    private static final String[] surfix = new String[]{"Jr","Sr","III","N/A"};

    ActivityResultLauncher<Intent> someActivityResultLaunchers = registerForActivityResult(
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
            });



}


