package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    AutoCompleteTextView surfixInpuT;
    TextView signupbtn;
    EditText firstName, emailNum, passWord, lastName, dateOfBirth;
    Button createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailNum = findViewById(R.id.inputmobile);
        passWord = findViewById(R.id.inputPassword);
        dateOfBirth = findViewById(R.id.inputDateofBirth);

        surfixInpuT = findViewById(R.id.surfixinput);


        AutoCompleteTextView gender1 =(AutoCompleteTextView)findViewById(R.id.genderinput);
        AutoCompleteTextView surfix1 =(AutoCompleteTextView)findViewById(R.id.surfixinput);
        AutoCompleteTextView cell1 =(AutoCompleteTextView)findViewById(R.id.networkproviderinput);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,gender);
        gender1.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,surfix);
        surfix1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,Cellularnet);
        cell1.setAdapter(adapter2);



surfix1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        surfix1.showDropDown();


    }
});
        gender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender1.showDropDown();
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
                        String date = day + "/" + month + "/" + year;
                        dateOfBirth.setText(date);
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
                                intent.putExtra("mobile", inputmobile.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("email", "'0" + emailNum.getText().toString());
                                intent.putExtra("password", passWord.getText().toString());
                                intent.putExtra("firstname", firstName.getText().toString());
                                intent.putExtra("lastname", lastName.getText().toString());
                                startActivity(intent);
                            }
                        }
                );


            }
        });


    }
    private static final String[] gender = new String[]{"Male","Female","3x a day"};
    private static final String[] surfix = new String[]{"Jr","Sr","III","N/A"};
    private static final String[] Cellularnet = new String[]{"SMART/TNT/SUN","GLOBE/TM/GOMO","DITO"};

}