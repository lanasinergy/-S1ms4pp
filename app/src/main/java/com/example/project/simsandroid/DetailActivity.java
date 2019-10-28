package com.example.project.simsandroid;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.project.simsandroid.adapter.PagerAdapter;
import com.example.project.simsandroid.data.model.Leads;
import com.example.project.simsandroid.ui.home.DetailLeadFragment;
import com.example.project.simsandroid.ui.home.HomeFragment;
import com.example.project.simsandroid.ui.home.TenderSalesFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Server;

public class DetailActivity extends AppCompatActivity implements DetailLeadFragment.OnFragmentInteractionListener, TenderSalesFragment.OnFragmentInteractionListener {

    EditText etLead;
    String etLead2;
    TextView tvpresales, tvlead, tvopp, tvowner;
    Button btnEditLead;
    String lead_edit, etOppName2, etClosing_date2;
    HorizontalStepView horizontalsStepView;
    List<StepBean> sources = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Leads lead = (Leads) getIntent().getSerializableExtra(HomeFragment.LEADS1);
        tvpresales = findViewById(R.id.presales_detail_lead);
        etLead = findViewById(R.id.edit_lead_id_fragment);
        etLead.setText(lead.getLead_id());
        etLead2 = etLead.getText().toString().trim();
        etLead.setVisibility(View.GONE);
        tvlead = findViewById(R.id.detail_lead);
        tvlead.setText(lead.getLead_id());
        tvopp = findViewById(R.id.opp_name_detail);
        tvowner = findViewById(R.id.owner_detail);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Solution Design"));
        tabLayout.addTab(tabLayout.newTab().setText("Tender Process"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        horizontalsStepView = findViewById(R.id.horizontalStepview);
        Log.i(lead.getResult(), "onCreateView: ");
        if (lead.getResult().equals("INITIAL")) {
            sources.add(new StepBean("Initial", 0));
            sources.add(new StepBean("Open", -1));
            sources.add(new StepBean("SD", -1));
            sources.add(new StepBean("TP", -1));
            sources.add(new StepBean("Win/Lose", -1));
        } else if (lead.getResult().equals("OPEN")) {
            sources.add(new StepBean("Initial", 1));
            sources.add(new StepBean("Open", 0));
            sources.add(new StepBean("SD", -1));
            sources.add(new StepBean("TP", -1));
            sources.add(new StepBean("Win/Lose", -1));
        } else if (lead.getResult().equals("SOLUTION DESIGN")) {
            sources.add(new StepBean("Initial", 1));
            sources.add(new StepBean("Open", 1));
            sources.add(new StepBean("SD", 0));
            sources.add(new StepBean("TP", -1));
            sources.add(new StepBean("Win/Lose", -1));
        } else if (lead.getResult().equals("TENDER PROCESS")) {
            sources.add(new StepBean("Initial", 1));
            sources.add(new StepBean("Open", 1));
            sources.add(new StepBean("SD", 1));
            sources.add(new StepBean("TP", 0));
            sources.add(new StepBean("Win/Lose", -1));
        } else if (lead.getResult().equals("WIN")) {
            sources.add(new StepBean("Initial", 1));
            sources.add(new StepBean("Open", 1));
            sources.add(new StepBean("SD", 1));
            sources.add(new StepBean("TP", 1));
            sources.add(new StepBean("Win", 0));
        } else if (lead.getResult().equals("LOSE")) {
            sources.add(new StepBean("Initial", 1));
            sources.add(new StepBean("Open", 1));
            sources.add(new StepBean("SD", 1));
            sources.add(new StepBean("TP", 1));
            sources.add(new StepBean("Lose", 0));
        }

        horizontalsStepView.setStepViewTexts(sources)
                .setTextSize(8)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#b5bdc9"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.group_8))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.group_9))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.group_7));

        tampilpresales();
        tampildetail();
    }

    private void tampilpresales() {
        final JSONObject jobj = new JSONObject();
        final String presales = "null";
        try {
            jobj.put("etLead", etLead2);

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
                        tvpresales.setText(presales.getString("name"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void tampildetail() {
        final JSONObject jobj = new JSONObject();
        final String presales = "null";
        try {
            jobj.put("etLead", etLead2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Server.URL_detail_lead, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        JSONObject detail = jObj.getJSONObject("lead_id");
                        tvowner.setText(detail.getString("name"));
                        tvopp.setText(detail.getString("opp_name"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
