package com.example.quarta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class LoanCalculator extends AppCompatActivity {

    String[] brokerArray = new String[50];
    double[] IRarray = new double[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);

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
                double interest = 0;
                double totalpayment = 0;

                //When the user does not enter any number it will automatically sets it to 0
                if(LoanInput.getText().toString().length() == 0){
                    LoanInput.setText("0");
                }
                if(inputLateDays.getText().toString().length() == 0){
                    inputLateDays.setText("0");
                }
                if(BrokerInput.getText().toString() == null) {
                    BrokerInput.setText("None");
                    interest = 0.1 * loan;
                    Log.d("None","if condition none");
                }
                String brokerVal = BrokerInput.getText().toString();
                LoanCalculatorData();
                int result = binarySearch(brokerArray, brokerVal);
                if(result == -1){
                    BrokerInput.setText("Invalid");
                    interest = 0.1 * loan;
                }else{
                    interest = IRarray[result] * loan;
                }

                loan = Double.parseDouble(LoanInput.getText().toString());
                latedays = Integer.parseInt(inputLateDays.getText().toString());

                latefee = latefee * latedays;
                totalpayment = loan + latefee + interest;

                OutputInterest.setText(String.format("%.2f",interest));
                outputLateFee.setText(String.valueOf(latefee));
                outputTotalPayment.setText(String.format("%.2f",totalpayment));


            }
        });

    }private void LoanCalculatorData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyEstp1uGxL8ShnbQu_DnPXRDQYP0QhYe37bqgmOco05fg6MQtkPEHSZKpp7bIooi4y/exec?action=loanCalculatorData",
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

            Toast.makeText(this, String.valueOf(IRarray[1]), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    static int binarySearch(String[] arr, String x)
    {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int res = x.compareTo(arr[m]);

            // Check if x is present at mid
            if (res == 0)
                return m;

            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return -1;
    }
}