package com.example.project.simsandroid.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.simsandroid.R;
import com.example.project.simsandroid.adapter.LeadRegisterAdapter;
import com.example.project.simsandroid.data.model.Leads;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Server;

public class HomeFragment extends Fragment implements LeadRegisterAdapter.ILeadAdapter {

    private HomeViewModel homeViewModel;

    public static final String LEADS1 = "leads";
    public static final int REQUEST_CODE_EDIT = 99;
    public static String lead_id;
    public static String name_sales;
    public List<Leads> lList = new ArrayList<>();
    public List<String> SalesName, ContactName, PresalesName;
    ProgressBar progresslead;
    Spinner spinContact, spinnSales, spinPresales;
    TextView mName, mEmail, mlead, mopp, mcoba, mstatus, mAmount, tvLead, ivAssign;
    LeadRegisterAdapter leadsAdapter;
    Button btnAddlead, btnPresales;
    ArrayAdapter<String> SpinnerAdapter;
    DatePickerDialog.OnDateSetListener date;
    EditText searchText, eLead;
    FloatingActionButton fabutton;
    String cek_status;
    int itemPos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        mopp = root.findViewById(R.id.opty_name);
        mstatus = root.findViewById(R.id.mStatus);
        searchText = root.findViewById(R.id.search_view);
        mAmount = root.findViewById(R.id.maamount);
        btnPresales = root.findViewById(R.id.btn_presales);
        ivAssign = root.findViewById(R.id.iv_assign);

        tampilkanlead();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        leadsAdapter = new LeadRegisterAdapter(lList, this, getActivity());
        recyclerView.setAdapter(leadsAdapter);
        //error syad

        SalesName = new ArrayList<String>();
        ContactName = new ArrayList<String>();
        PresalesName = new ArrayList<String>();

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        //iku maeng error e guduk iku, error e iki
        // opo o kok nyeluk iki
        //iku fabutton e null ngno a?
        //comment sek ae ndapopo
        //yo ndang
        //oalah tapi maeng amu ngganti sg getContext dadi getActivity baru iso

        //ogak din, eh mbuh ding. soale error sng adapter iku pas hpmu sng sijine, gusuk hpe seng  iki
        // log e sek nyantol
        //tak dudui sg mok
        fabutton = root.findViewById(R.id.fab);
        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addlead();
            }
        });

        return root;

    }

    private void filter(String s) {
        ArrayList<Leads> filteredList = new ArrayList<>();

        for (Leads item : lList) {
            if (item.getLead_id().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
        }

        leadsAdapter.filterList(filteredList);
    }

    private void addlead() {
//        Intent intent = new Intent(LeadRegister.this, AddLeadActivity.class);
//        intent.putExtra("addlead", "coba");
//        startActivity(intent);
    }

    private void tampilkanlead() {
        final JSONObject jobj = new JSONObject();
        final String lead_id = "null";
        final String opp_name = "null";
        final String contact = "null";
        final String sales = "null";
        final String status = "null";
        final String amount = "null";
        try {
            jobj.put("lead_id", lead_id);
            jobj.put("opp_name", opp_name);
            jobj.put("contact", contact);
            jobj.put("sales", sales);
            jobj.put("status", status);
            jobj.put("amounts", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, Server.URL_Lead, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        JSONArray jray = jObj.getJSONArray("lead");
                        JSONArray jray_user = jObj.getJSONArray("sales_list");
                        JSONArray jray_contact = jObj.getJSONArray("contact_list");
                        JSONArray jray_presales = jObj.getJSONArray("presales_list");

                        Log.i("response", String.valueOf(jray.length()));

                        if (response.length() > 0) {
                            for (int i = 0; i < jray.length(); i++) {
                                JSONObject o = jray.getJSONObject(i);
                                Leads item = new Leads();
                                item.setLead_id(o.getString("lead_id"));
                                item.setOpp_name(o.getString("opp_name"));
                                item.setNik(o.getString("name"));
                                item.setId_customer(o.getString("customer_legal_name"));
                                item.setClosing_date(o.getString("closing_dates"));
                                item.setResult(o.getString("results"));
                                item.setAmount(o.getString("amounts"));
                                lList.add(item);
                                Log.i(item.getResult(), "onResponse: ");
                            }
                            leadsAdapter.notifyDataSetChanged();
                        }
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
                params.put("lead_id", lead_id);
                params.put("opp_name", opp_name);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public void doClick(int pos) {

    }

    @Override
    public void doEdit(int pos) {

    }

    @Override
    public void doAssign(int pos) {

    }
}