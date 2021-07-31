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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    AutoCompleteTextView surfixInpuT;
    TextView signupbtn;
    EditText firstName, middleName, lastName, sexes, dateOfBirth, emailAdd, Address, SimNetwork, Number, passWord;
    Button createAcc;
    ImageView idImage2;
    Uri urs2;
    String encImage;
    RadioGroup rgSex;
    RadioButton rgBtnSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        rgSex = findViewById(R.id.sexGroup);
        middleName = findViewById(R.id.middleNameSignUp);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);

        dateOfBirth = findViewById(R.id.inputDateofBirth);
        emailAdd = findViewById(R.id.Inputemail);
        Address = findViewById(R.id.Inputadress);
        SimNetwork = findViewById(R.id.networkproviderinput);
        Number = findViewById(R.id.inputmobile);
        passWord = findViewById(R.id.inputPassword);
        surfixInpuT = findViewById(R.id.surfixinput);

        idImage2 = findViewById(R.id.idImageinput);
        idImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //Intent intent = new Intent(uploadImageTest.this, pickpick.class);
                someActivityResultLaunchers.launch(intent);
            }
        });


        AutoCompleteTextView surfix1 = (AutoCompleteTextView) findViewById(R.id.surfixinput);
        AutoCompleteTextView cell1 = (AutoCompleteTextView) findViewById(R.id.networkproviderinput);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, surfix);
        surfix1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Cellularnet);
        cell1.setAdapter(adapter2);


        surfix1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfix1.showDropDown();
            }
        });


        cell1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cell1.showDropDown();
            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = month + "/" + day + "/" + year;
                        dateOfBirth.setText(date);
                        Toast.makeText(SignUp.this, dateOfBirth.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });


        final EditText inputmobile = findViewById(R.id.inputmobile);
        Button buttonOTP = findViewById(R.id.signupbutton);

        final ProgressBar progressBar = findViewById(R.id.progressbar);


        buttonOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lods=rgSex.getCheckedRadioButtonId();
                rgBtnSex = findViewById(lods);

                Toast.makeText(SignUp.this, String.valueOf(lods), Toast.LENGTH_SHORT).show();
                //Toast.makeText(SignUp.this, String.valueOf(sexId), Toast.LENGTH_SHORT).show();

                Toast.makeText(SignUp.this, rgBtnSex.getText().toString(), Toast.LENGTH_SHORT).show();
                if (urs2 != null) {
                    InputStream imageStream = null;

                    try {
                        imageStream = getContentResolver().openInputStream(urs2);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] bytes = baos.toByteArray();
                    encImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                } else {
                    Toast.makeText(SignUp.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    return;
                }


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
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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




                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("mobile", inputmobile.getText().toString());
                                intent.putExtra("email", emailAdd.getText().toString());
                                intent.putExtra("password", passWord.getText().toString());
                                intent.putExtra("firstname", firstName.getText().toString());
                                intent.putExtra("middlename",middleName.getText().toString());
                                intent.putExtra("lastname", lastName.getText().toString());
                                intent.putExtra("suffix", surfixInpuT.getText().toString());
                                intent.putExtra("dateOfBirth", dateOfBirth.getText().toString());
                                intent.putExtra("address", Address.getText().toString());
                                intent.putExtra("networkProvider", SimNetwork.getText().toString());
                                intent.putExtra("sex", rgBtnSex.getText().toString());


                                SharedPreferences myPref = getSharedPreferences("ImageBase64", MODE_PRIVATE);
                                SharedPreferences.Editor myPrefEdit = myPref.edit();
                                myPrefEdit.putString("Base64Image", encImage);
                                myPrefEdit.apply();

                                startActivity(intent);
                            }
                        }
                );


            }
        });


    }

    private static final String[] gender = new String[]{"Male", "Female", "Non-binary"};
    private static final String[] surfix = new String[]{"Jr", "Sr", "III", "N/A"};
    private static final String[] Cellularnet = new String[]{"SMART/TNT/SUN", "GLOBE/TM/GOMO", "DITO"};


    ActivityResultLauncher<Intent> someActivityResultLaunchers = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        urs2 = data.getData();
                        idImage2.setImageURI(urs2);
                    }
                }
            });

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }


}