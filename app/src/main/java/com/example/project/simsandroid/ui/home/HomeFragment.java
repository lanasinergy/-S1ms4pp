package com.example.project.simsandroid.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.simsandroid.DetailActivity;
import com.example.project.simsandroid.HomeActivity;
import com.example.project.simsandroid.LeadPage;
import com.example.project.simsandroid.R;
import com.example.project.simsandroid.adapter.LeadRegisterAdapter;
import com.example.project.simsandroid.coba_coba;
import com.example.project.simsandroid.data.model.Leads;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.Server;

public class HomeFragment extends Fragment implements LeadRegisterAdapter.ILeadAdapter {

    private HomeViewModel homeViewModel;

    SwipeRefreshLayout swipeRefreshLayout;

    public static final String LEADS1 = "leads";
    public static final int REQUEST_CODE_EDIT = 99;
    ArrayList<Integer> total_amount = new ArrayList<>();
    public static String lead_id;
    public static String name_sales;
    public List<Leads> lList = new ArrayList<>();
    public List<String> SalesName, ContactName, PresalesName;
    ProgressBar progresslead;
    Spinner spinContact, spinnSales, spinPresales;
    TextView mName, mEmail, mlead, mopp, mcoba, mstatus, mAmount, tvLead, ivAssign, tvTAmount;
    LeadRegisterAdapter leadsAdapter;
    Button btnAddlead, btnPresales;
    ArrayAdapter<String> SpinnerAdapter;
    DatePickerDialog.OnDateSetListener date;
    EditText searchText, eLead;
    FloatingActionButton fabutton;
    String cek_status;
    int itemPos;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_home);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        mopp = root.findViewById(R.id.opty_name);
        mstatus = root.findViewById(R.id.mStatus);
        searchText = root.findViewById(R.id.search_view);
        mAmount = root.findViewById(R.id.maamount);
//        btnPresales = root.findViewById(R.id.btn_presales);
        ivAssign = root.findViewById(R.id.iv_assign);
        tvTAmount = root.findViewById(R.id.total_amount);

        tampilkanlead();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        leadsAdapter = new LeadRegisterAdapter(this, lList);
        recyclerView.setAdapter(leadsAdapter);

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
        int total2 = 0;
        for (Leads item : lList) {
            if (item.getLead_id().toLowerCase().contains(s.toLowerCase()) || item.getNik().toLowerCase().contains(s.toLowerCase()) ||
                    item.getResult().toLowerCase().contains(s.toLowerCase()) || item.getOpp_name().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
                total_amount.add(item.getAmount());
                total2 += item.getAmount();
            }
        }
        tvTAmount.setText(formatRupiah.format(total2));
        leadsAdapter.filterList(filteredList);
    }

    private void addlead() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra("newLead", "new_lead");
        startActivity(intent);
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
                            int total2 = 0;
                            for (int i = 0; i < jray.length(); i++) {
                                JSONObject o = jray.getJSONObject(i);
                                Leads item = new Leads();
                                item.setLead_id(o.getString("lead_id"));
                                item.setOpp_name(o.getString("opp_name"));
                                item.setNik(o.getString("name"));
                                item.setId_customer(o.getString("customer_legal_name"));
                                item.setClosing_date(o.getString("closing_dates"));
                                item.setResult(o.getString("results"));
                                item.setAmount(o.getInt("amounts"));

                                total_amount.add(o.getInt("amounts"));
                                total2 += total_amount.get(i);
                                tvTAmount.setText(formatRupiah.format(total2));

                                lList.add(item);
                                Log.i(String.valueOf(o.getInt("amounts")), "onResponse: ");
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
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(LEADS1, leadsAdapter.getItem(pos));
        intent.putExtra("get_id", "id");
        startActivity(intent);
    }

    @Override
    public void doEdit(int pos) {
        itemPos = pos;
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra(LEADS1, leadsAdapter.getItem(pos));
        intent.putExtra("get_id_edit", "id");
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    public void doAssign(int pos) {
        PresalesName = new ArrayList<String>();

        itemPos = pos;
        Leads lead = leadsAdapter.getItem(pos);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.add_lead, null);
        mBuilder.setView(mView);

        spinPresales = mView.findViewById(R.id.spinner_presales);
        btnPresales = mView.findViewById(R.id.btn_presales);
        tvLead = mView.findViewById(R.id.lead_id);


        tvLead.setText(lead.getLead_id());
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        final JSONObject jobj = new JSONObject();
        final String presales = null;
        try {
            jobj.put("presales", presales);
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
                        JSONArray jray_lead = jObj.getJSONArray("lead");
                        JSONArray jray_presales = jObj.getJSONArray("presales_list");
                        if (response.length() > 0) {


                            for (int i = 0; i < jray_presales.length(); i++) {
                                JSONObject presales = jray_presales.getJSONObject(i);
                                String presaleses = presales.getString("name");
                                PresalesName.add(presaleses);

                            }
                            Log.i(jray_presales.toString(), "onResponse: ");
                            spinPresales.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, PresalesName));

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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);

        btnPresales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignPresales();
                dialog.dismiss();
            }
        });
    }

    private void assignPresales() {
        final String presales = spinPresales.getSelectedItem().toString().trim();
        String lead_id = tvLead.getText().toString().trim();

        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("spinner_presales", presales);
            jobj.put("lead_id", lead_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_assign, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                JSONObject jObj = response;

                /*Intent intent = new Intent(getActivity(), LeadPage.class);
                Toast.makeText(getActivity(), "Successfully Add Presales", Toast.LENGTH_LONG).show();
                startActivity(intent);*/
                Toast.makeText(getContext(), "Successfully Add Presales", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}