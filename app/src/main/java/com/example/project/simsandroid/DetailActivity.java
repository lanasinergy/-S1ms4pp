package com.example.project.simsandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.simsandroid.data.model.Leads;
import com.example.project.simsandroid.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import util.Server;

public class DetailActivity extends AppCompatActivity {
    EditText etOppName, etClosing_date;
    TextView tvLead, tvOppName;
    Button btnEditLead;
    String lead_edit, etOppName2, etClosing_date2;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_detail_lead);

        Leads lead = (Leads) getIntent().getSerializableExtra(HomeFragment.LEADS1);
        setTitle(lead.getLead_id());
        tvLead = findViewById(R.id.edit_lead);

        tvLead.setText(lead.getLead_id());
        tvOppName = findViewById(R.id.opty_name);
        tvOppName.setText(lead.getOpp_name());
        etOppName = findViewById(R.id.edit_opp_name);
        etOppName.setText(lead.getOpp_name());
        etClosing_date = findViewById(R.id.edit_closing_date);
        etClosing_date.setText(lead.getClosing_date());
        btnEditLead = findViewById(R.id.btnEditLead);


        //button edit
        btnEditLead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                etOppName2 = etOppName.getText().toString().trim();
                etClosing_date2 = etClosing_date.getText().toString().trim();
                lead_edit = tvLead.getText().toString().trim();
                updatelead();

            }
        });

        //tanggal
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatelabel();
            }
        };

        etClosing_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DetailActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    //tanggal
    private void updatelabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etClosing_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updatelead() {
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("etOppName", etOppName2);
            jobj.put("etClosing_date", etClosing_date2);
            jobj.put("tvLead", lead_edit);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_updateLead, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                JSONObject jObj = response;
                String success = null;
                try {
                    success = jObj.getString("success");

                    if (success.equals("1")) {
                        Intent intent = new Intent(DetailActivity.this, HomeFragment.class);
                        Toast.makeText(DetailActivity.this, "Lead Id Updated Successfully :)", Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    } else {
                        Toast.makeText(DetailActivity.this, "salah!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(DetailActivity.this, "Ora oleh data", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                        }
                        Toast.makeText(DetailActivity.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
}
