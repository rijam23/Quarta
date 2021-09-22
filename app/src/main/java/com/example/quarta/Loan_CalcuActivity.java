package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Loan_CalcuActivity extends AppCompatActivity {

    String[] brokerArray = new String[50];
    double[] IRarray = new double[50];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calcu);

        Button btnEnter = findViewById(R.id.btnEnter);
        //When the User Clicks the button Enter
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText LoanInput = findViewById(R.id.LoanInput);
                TextView OutputInterest = findViewById(R.id.OutputInterest);
                TextView outputLateFee = findViewById(R.id.outputLateFee);
                EditText inputLateDays = findViewById(R.id.inputLateDays);
                TextView outputTotalPayment = findViewById(R.id.outputTotalPayment);
                EditText BrokerInput = findViewById(R.id.brokerInput);


                int latedays = 0;
                double loan = 0;
                double latefee = 35;
                double interest = 0.1;
                double totalpayment = 0;
                String brokerVal = null;
                //When the user does not enter any number it will automatically sets it to 0
                if(LoanInput.getText().toString().length() == 0){
                    LoanInput.setText("0");
                }
                if(inputLateDays.getText().toString().length() == 0){
                    inputLateDays.setText("0");
                }

                if (BrokerInput.getText().toString().length() == 0){

                    BrokerInput.setText("None");

                }else{
                    boolean ans = false;
                    LoanCalculatorData();
                    brokerVal = BrokerInput.getText().toString();

                    for(int i = 0; i < brokerArray.length ; i++){
                        if(brokerArray[i].equals(brokerVal)) {
                            interest = IRarray[i];
                            ans = true;
                            break;
                        }
                    }if(ans){
                        BrokerInput.setText(brokerVal);
                    }else{
                        BrokerInput.setText("Invalid");
                    }

                }
                loan = Double.parseDouble(LoanInput.getText().toString());
                latedays = Integer.parseInt(inputLateDays.getText().toString());

                interest = interest * loan;
                latefee = latefee * latedays;
                totalpayment = loan + latefee + interest;

                OutputInterest.setText(String.format("%.2f",interest));
                outputLateFee.setText(String.valueOf(latefee));
                outputTotalPayment.setText(String.format("%.2f",totalpayment));


            }
        });

    }private void LoanCalculatorData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbxV3CQKgHm8escJPg947NX5bcmQO-xfV1YiJmGU8IdVTE6d6Is6_OAcL3uwYNnNo47Y/exec?action=loanCalculatorData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response2) {
                        jsonParse(response2);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void jsonParse(String jsonResponce2){

        try {

            JSONObject jobject = new JSONObject(jsonResponce2);
            JSONArray jsonArray = jobject.getJSONArray("items");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String Broker_code = item.getString("Broker Code");
                brokerArray[i] = Broker_code;
                double IR = item.getDouble("IR");
                IRarray [i] = IR;

            }

            //Log.d(brokerArray[1], String.valueOf(IRarray [1]));
            Toast.makeText(this,String.valueOf(IRarray[4]),Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}