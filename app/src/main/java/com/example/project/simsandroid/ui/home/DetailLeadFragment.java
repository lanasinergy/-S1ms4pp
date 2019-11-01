package com.example.project.simsandroid.ui.home;

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
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.project.simsandroid.DetailActivity;
import com.example.project.simsandroid.R;
import com.example.project.simsandroid.data.model.Leads;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.Server;

public class DetailLeadFragment extends Fragment {
    TextView tvLead, tvOppName, tvLead2, tvpresales, tvproposed, tvassessment, tvproof, tvproject_size, tvpriority;
    String lead_edit, etOppName2, etLead2, etassesment2, etproposed2, etproof2, etproject_budget2, etpriority, etproject_size,
            etNik2, tvassessment2, tvproposed2, tvproof2, tvproject_size2, tvpriority2, etassess_before2, etpro_before2,
            etproof_before2, etpb_before2, etprio_before2, etpj_before2, ettimeasses_before2, ettimepro_before2, ettimeproof_before2, etassesment3;
    EditText etLead, etassesment, etproposed, etproject_budget, etproof, etNik, etassess_before, etpro_before, etproof_before, etpb_before, etprio_before, etpj_before, ettimeasses_before, ettimepro_before, ettimeproof_before;
    Spinner spinnerPriority, spinnerProjectSize;
    Button btnSubmitsd, btnTp;
    HorizontalStepView horizontalsStepView;
    List<StepBean> sources = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public DetailLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_detail_lead, container, false);

        Leads lead = (Leads) getActivity().getIntent().getSerializableExtra(HomeFragment.LEADS1);
        etLead = view.findViewById(R.id.edit_lead_id_fragment);
        etLead.setText(lead.getLead_id());
        etLead2 = etLead.getText().toString().trim();
        etLead.setVisibility(View.GONE);
        etassesment = view.findViewById(R.id.edit_assesment);
        etproposed = view.findViewById(R.id.edit_proposed);
        etproof = view.findViewById(R.id.edit_proof);
        etproject_budget = view.findViewById(R.id.edit_project_budget);
        etproject_budget.addTextChangedListener(onTextChangedListener());
        spinnerPriority = view.findViewById(R.id.spinner_priority);
        spinnerProjectSize = view.findViewById(R.id.spinner_project_size);
        btnSubmitsd = view.findViewById(R.id.btn_submit_sd);
        tvassessment = view.findViewById(R.id.tvtimeassessment);
        tvassessment2 = tvassessment.getText().toString().trim();
        tvproof = view.findViewById(R.id.tvtimeproof);
        tvproof2 = tvproof.getText().toString().trim();
        tvproposed = view.findViewById(R.id.tvtimeproposed);
        tvproposed2 = tvproposed.getText().toString().trim();
        tvpriority = view.findViewById(R.id.tvpriority);
        tvpriority2 = tvpriority.getText().toString().trim();
        tvproject_size = view.findViewById(R.id.tvproject_size);
        tvproject_size2 = tvproject_size.getText().toString().trim();
        etassess_before = view.findViewById(R.id.tv_assessment_before);
        etassess_before2 = etassess_before.getText().toString().trim();
        etpro_before = view.findViewById(R.id.tv_propoosed_before);
        etpro_before2 = etpro_before.getText().toString().trim();
        etproof_before = view.findViewById(R.id.tv_proof_before);
        etproof_before2 = etproof_before.getText().toString().trim();
        etpb_before = view.findViewById(R.id.tv_pb_before);
        etpb_before2 = etpb_before.getText().toString().trim();
        etprio_before = view.findViewById(R.id.tv_prio_before);
        etprio_before2 = etprio_before.getText().toString().trim();
        etpj_before = view.findViewById(R.id.tv_pj_before);
        etpj_before2 = etpj_before.getText().toString().trim();
        ettimeasses_before = view.findViewById(R.id.ettimeasses_before);
        ettimeasses_before2 = ettimeasses_before.getText().toString().trim();
        ettimepro_before = view.findViewById(R.id.ettimepropose_before);
        ettimepro_before2 = ettimepro_before.getText().toString().trim();
        ettimeproof_before = view.findViewById(R.id.ettimeproof_before);
        ettimeproof_before2 = ettimeproof_before.getText().toString().trim();
        btnSubmitsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etassesment2 = etassesment.getText().toString().trim();
                etproposed2 = etproposed.getText().toString().trim();
                etproof2 = etproof.getText().toString();
                etproject_budget2 = etproject_budget.getText().toString().trim().replaceAll(",", "");
                etpriority = spinnerPriority.getSelectedItem().toString().trim();
                etproject_size = spinnerProjectSize.getSelectedItem().toString().trim();
                updatesd();
            }
        });

        btnTp = view.findViewById(R.id.btn_tp);
        btnTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                etLead2 = etLead.getText().toString().trim();
                raise_to_tender();
            }
        });
        /*etNik = view.findViewById(R.id.edit_nik_fragment);
        etNik.setVisibility(View.GONE);
*/
        tampilkanpresales();

        return view;
    }

    private void raise_to_tender() {
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("etLead", etLead2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_raise_to_tender, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());

                Intent intent = new Intent(getActivity(), HomeFragment.class);
                Toast.makeText(getActivity(), "Raise to Tender Successfully :)", Toast.LENGTH_LONG).show();
                startActivity(intent);

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

    private void tampilkanpresales() {
        final JSONObject jobj = new JSONObject();
        final String presales = "null";
        try {
            jobj.put("etLead", etLead2);
            jobj.put("ettimeasses_before", ettimeasses_before2);
            jobj.put("ettimepro_before", ettimepro_before2);
            jobj.put("ettimeproof_before", ettimeproof_before2);
            jobj.put("etassess_before", etassess_before2);
            jobj.put("etpro_before", etpro_before2);
            jobj.put("etproof_before", etproof_before2);
            jobj.put("etpb_before", etpb_before2);
            jobj.put("etprio_before", etprio_before2);
            jobj.put("etpj_before2", etpj_before2);
            jobj.put("etassesment", etassesment2);
            jobj.put("etproposed", etproposed2);
            jobj.put("etproof", etproof2);
            jobj.put("etproject_budget", etproject_budget2);
            jobj.put("tvproposed", tvproposed2);
            jobj.put("tvassessment", tvassessment2);
            jobj.put("tvproof", tvproof2);
            jobj.put("tvpriority", tvpriority2);
            jobj.put("tvproject_size", tvproject_size2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_tampil_sd, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        JSONObject presales = jObj.getJSONObject("presales_detail");
                        etassesment.setText(presales.getString("assessments"));
                        etproposed.setText(presales.getString("pds"));
                        etproof.setText(presales.getString("povs"));
                        etproject_budget.setText(presales.getString("pbs"));
                        tvassessment.setText(presales.getString("assessment_dates"));
                        tvproposed.setText(presales.getString("pd_dates"));
                        tvproof.setText(presales.getString("pov_dates"));
                        tvpriority.setText(presales.getString("prioritys"));
                        tvproject_size.setText(presales.getString("project_sizes"));
                        etassess_before.setText(presales.getString("assessments"));
                        etpro_before.setText(presales.getString("pds"));
                        etproof_before.setText(presales.getString("povs"));
                        etpb_before.setText(presales.getString("pbs"));
                        etprio_before.setText(presales.getString("prioritys"));
                        etpj_before.setText(presales.getString("project_sizes"));
                        ettimeasses_before.setText(presales.getString("assessment_dates"));
                        ettimepro_before.setText(presales.getString("pd_dates"));
                        ettimeproof_before.setText(presales.getString("pov_dates"));
                        etassess_before.setVisibility(View.GONE);
                        etpro_before.setVisibility(View.GONE);
                        etproof_before.setVisibility(View.GONE);
                        etpb_before.setVisibility(View.GONE);
                        etprio_before.setVisibility(View.GONE);
                        etpj_before.setVisibility(View.GONE);
                        ettimeproof_before.setVisibility(View.GONE);
                        ettimepro_before.setVisibility(View.GONE);
                        ettimeasses_before.setVisibility(View.GONE);

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
                params.put("lead_id", presales);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    private void updatesd() {
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("etassesment", etassesment2);
            jobj.put("etproposed", etproposed2);
            jobj.put("etproof", etproof2);
            jobj.put("etproject_budget", etproject_budget2);
            jobj.put("spinnerPriority", etpriority);
            jobj.put("spinnerProjectSize", etproject_size);
            jobj.put("etLead", etLead2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_update_sd, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                JSONObject jObj = response;
                String success = null;
                try {
                    success = jObj.getString("success");

                    if (success.equals("1")) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        Toast.makeText(getActivity(), "Solution Design Updated Successfully :)", Toast.LENGTH_LONG).show();
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
                etproject_budget.removeTextChangedListener(this);

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
                    etproject_budget.setText(formattedString);
                    etproject_budget.setSelection(etproject_budget.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etproject_budget.addTextChangedListener(this);
            }
        };
    }
}
