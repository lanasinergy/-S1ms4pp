package com.example.project.simsandroid.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.simsandroid.R;
import com.example.project.simsandroid.data.model.Leads;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import util.Server;


public class TenderSalesFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    Button btn_submit_tp, btn_result, btn_submit_result;
    EditText etno_doc, etsubmit_price, etdeal_price, etproject_name, submit_date, etLead, etquote;
    Spinner spinproject_class, spinwin_prob, spinresult;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    TextView tvproject_class, tvwin_prob, tvLead;
    String etLead2, etno_doc2, etsubmit_price2, etdeal_price2, etproject_name2, etsubmit_date2, etquote2, etproject_class, etwin_prob;


    public TenderSalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tender_sales, container, false);
        Leads lead = (Leads) getActivity().getIntent().getSerializableExtra(HomeFragment.LEADS1);
        btn_submit_tp = view.findViewById(R.id.btn_submit_tp);
        submit_date = view.findViewById(R.id.edittext_submit_date);
        etLead = view.findViewById(R.id.edit_lead_id_fragment);
        etLead.setText(lead.getLead_id());
        etLead2 = etLead.getText().toString().trim();
        etLead.setVisibility(View.GONE);
        etno_doc = view.findViewById(R.id.edittext_no_doc);
        etsubmit_price = view.findViewById(R.id.edittext_submit_price);
        etsubmit_price.addTextChangedListener(onTextChangedListener());
        spinproject_class = view.findViewById(R.id.spinner_project_class);
        etdeal_price = view.findViewById(R.id.edittext_deal_price);
        etdeal_price.addTextChangedListener(onTextChangedListener2());
        spinwin_prob = view.findViewById(R.id.spinner_win_prob);
        etproject_name = view.findViewById(R.id.edittext_project_name);
        etquote = view.findViewById(R.id.edittext_quote);
        tvproject_class = view.findViewById(R.id.tvproject_class);
        tvwin_prob = view.findViewById(R.id.tvwin_prob);
        btn_result = view.findViewById(R.id.btn_result);


        btn_submit_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etno_doc2 = etno_doc.getText().toString().trim();
                etsubmit_price2 = etsubmit_price.getText().toString().trim().replaceAll(",", "");
                etdeal_price2 = etdeal_price.getText().toString().trim().replaceAll(",", "");
                etproject_name2 = etproject_name.getText().toString().trim();
                etquote2 = etquote.getText().toString().trim();
                etproject_class = spinproject_class.getSelectedItem().toString().trim();
                etwin_prob = spinwin_prob.getSelectedItem().toString().trim();
                updatetp();
            }
        });

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
        submit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilresult();
            }
        });


        tampiltp();
        return view;
    }

    private void tampilresult() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//        View mView = getActivity().getLayoutInflater().inflate(R.layout.add_result, null);
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.fragment_tender_add_result, null);
        mBuilder.setView(mView);

        Leads lead = (Leads) getActivity().getIntent().getSerializableExtra(HomeFragment.LEADS1);

        spinresult = mView.findViewById(R.id.spinner_result);
        btn_submit_result = mView.findViewById(R.id.btn_submit_result);
        tvLead = mView.findViewById(R.id.lead_id);


        tvLead.setText(lead.getLead_id());
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        btn_submit_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit_result();
            }
        });
    }

    private void submit_result() {
        String hasil = spinresult.getSelectedItem().toString().trim();
        String lead_id = tvLead.getText().toString().trim();
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("spinner_result", hasil);
            jobj.put("lead_id", lead_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_update_result, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                JSONObject jObj = response;
                String success = null;
                try {
                    success = jObj.getString("success");

                    if (success.equals("1")) {
                        Intent intent = new Intent(getActivity(), HomeFragment.class);
                        Toast.makeText(getActivity(), "Update Result Successfully :)", Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "salah!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "Ora oleh data", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
                Toast.makeText(getActivity(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void tampiltp() {
        final JSONObject jobj = new JSONObject();
        final String tp_detail = "null";
        try {
            jobj.put("etLead", etLead2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_tampil_tp, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        JSONObject tp_detail = jObj.getJSONObject("tp_detail");
                        etno_doc.setText(tp_detail.getString("auction_numbers"));
                        etsubmit_price.setText(tp_detail.getString("submit_prices"));
                        etquote.setText(tp_detail.getString("quote_numbers2"));
                        etproject_name.setText(tp_detail.getString("project_names"));
                        submit_date.setText(tp_detail.getString("submit_dates"));
                        tvwin_prob.setText(tp_detail.getString("win_probs"));
                        etdeal_price.setText(tp_detail.getString("deal_prices"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lead_id", tp_detail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }


    private void updatetp() {
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("etno_doc", etno_doc2);
            jobj.put("etsubmit_price", etsubmit_price2);
            jobj.put("submit_date", etsubmit_date2);
            jobj.put("spinproject_class", etproject_class);
            jobj.put("spinwin_prob", etwin_prob);
            jobj.put("etquote", etquote2);
            jobj.put("etdeal_price", etdeal_price2);
            jobj.put("etLead", etLead2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_update_tp, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                JSONObject jObj = response;
                String success = null;
                try {
                    success = jObj.getString("success");

                    if (success.equals("1")) {
                        Intent intent = new Intent(getActivity(), HomeFragment.class);
                        Toast.makeText(getActivity(), "Tender Process Updated Successfully :)", Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "salah!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "Ora oleh data", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void updatelabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        submit_date.setText(sdf.format(myCalendar.getTime()));
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etsubmit_price.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }

                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etsubmit_price.setText(formattedString);
                    etsubmit_price.setSelection(etsubmit_price.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etsubmit_price.addTextChangedListener(this);
            }
        };
    }

    private TextWatcher onTextChangedListener2() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etdeal_price.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }

                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etdeal_price.setText(formattedString);
                    etdeal_price.setSelection(etdeal_price.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etdeal_price.addTextChangedListener(this);
            }
        };
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
