package com.example.quarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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
                                intent.putExtra("email",emailNum.getText().toString());
                                intent.putExtra("password",passWord.getText().toString());
                                intent.putExtra("firstname",firstName.getText().toString());
                                intent.putExtra("lastname",lastName.getText().toString());
                                startActivity(intent);
                            }
                        }
                );



            }
        });
        

    }


    ////Transparent Status Bar
}