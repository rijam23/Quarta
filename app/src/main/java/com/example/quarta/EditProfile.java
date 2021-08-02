package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {
EditText dateofbirthh;
ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


       dateofbirthh = findViewById(R.id.dateofbirthEdit);
       backbutton = findViewById(R.id.editbackbutton);

        AutoCompleteTextView surfix1 = (AutoCompleteTextView) findViewById(R.id.surfixinput);



        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, surfix);
        surfix1.setAdapter(adapter1);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
            }
        });

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

    }
    private static final String[] surfix = new String[]{"Jr", "Sr", "III", "N/A"};
}