package com.example.project.simsandroid;


import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Server;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    TextView tvLeadRegister, tvOpen, tvWin, tvLose;
    List<Integer> warna_status = new ArrayList<>();


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvLeadRegister = root.findViewById(R.id.lead_register);
        tvOpen = root.findViewById(R.id.open);
        tvWin = root.findViewById(R.id.win);
        tvLose = root.findViewById(R.id.lose);

//        tvLeadRegister.setText("cek");

        loadData();
        return root;
    }

    private void loadData() {
        final JSONObject jobj = new JSONObject();

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, Server.URL_pie_chart, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    JSONArray coba = jObj.getJSONArray("total");
                    JSONArray amount_lead = jObj.getJSONArray("total_amount");
                    JSONArray total_lead = jObj.getJSONArray("total_leads");
                    JSONArray result_lead_total = jObj.getJSONArray("results_total");
                    Log.i(String.valueOf(coba), "onResponse: ");

                    PieChart pieChart = getView().findViewById(R.id.pieChart);
                    LineChart lineChart = getView().findViewById(R.id.lineChart);
                    BarChart barChart = getView().findViewById(R.id.barChart);

                    if (response.length() > 0) {

                        List<PieEntry> xVals = new ArrayList<>();
                        List<Entry> lineEntries = new ArrayList<>();
                        ArrayList<BarEntry> barEntries = new ArrayList<>();

                        for (int i = 0; i < coba.length(); i++) {
                            JSONObject item = coba.getJSONObject(i);
                            xVals.add(new PieEntry(item.getInt("result_total"), item.getString("results")));
                            warna_status.add(Color.parseColor(item.getString("color")));
                        }

                        for (int i = 0; i < amount_lead.length(); i++) {

                            JSONObject item = amount_lead.getJSONObject(i);
                            lineEntries.add(new Entry(item.getInt("month"), Float.parseFloat(item.getString("amount_total"))));

                        }

                        for (int i = 0; i < total_lead.length(); i++) {

                            JSONObject item = total_lead.getJSONObject(i);
                            barEntries.add(new BarEntry(item.getInt("month"), item.getInt("total_lead")));

                        }

                        PieDataSet dataSet = new PieDataSet(xVals, "");
                        dataSet.setColors(warna_status);

                        PieData data = new PieData(dataSet);
                        data.setValueTextSize(14f);
                        data.setValueTextColor(Color.WHITE);
                        data.setValueFormatter(new PercentFormatter());

                        pieChart.setData(data);
                        pieChart.setUsePercentValues(true);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setExtraOffsets(5, 0, 5, 0);
                        pieChart.setRotationEnabled(true);
                        pieChart.setHighlightPerTapEnabled(true);
                        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        pieChart.setDrawEntryLabels(false);
                        pieChart.setEntryLabelTextSize(12f);
                        pieChart.setHoleRadius(60f);
                        pieChart.setHoleColor(Color.parseColor("#383E40"));
                        pieChart.getLegend().setTextColor(Color.WHITE);

                        Legend l = pieChart.getLegend();
                        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        l.setDrawInside(false);
                        l.setXEntrySpace(6f);
                        l.setXOffset(6f);
                        l.setYEntrySpace(2f);
                        l.setYOffset(2f);

                        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        dataSet.setValueLinePart1OffsetPercentage(60f); /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
                        dataSet.setValueLinePart1Length(0.4f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
                        dataSet.setValueLinePart2Length(0.1f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */
                        pieChart.setExtraOffsets(0.f, 5.f, 0.f, 5.f);

                        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "August", "Sept", "Oct", "Nov", "Dec"};

                        LineDataSet lineDataSet = new LineDataSet(lineEntries, getString(R.string.total_amount));
                        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                        lineDataSet.setHighlightEnabled(true);
                        lineDataSet.setLineWidth(2);
                        lineDataSet.setCircleColor(Color.parseColor("#273a59"));
                        lineDataSet.setCircleRadius(6);
                        lineDataSet.setCircleHoleRadius(3);
                        lineDataSet.setDrawHighlightIndicators(true);
                        lineDataSet.setHighLightColor(Color.parseColor("#1b335c"));
                        lineDataSet.setValueTextSize(10);
                        lineDataSet.setColor(Color.parseColor("#1d50a3"));
                        lineDataSet.setValueTextColor(Color.WHITE);
                        lineDataSet.setFillColor(Color.parseColor("#14233b"));

                        LineData lineData = new LineData(lineDataSet);
                        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        lineChart.animateY(1000);
                        lineChart.getXAxis().setGranularityEnabled(true);
                        lineChart.getXAxis().setGranularity(1.0f);
                        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
                        lineChart.getDescription().setEnabled(false);
                        lineChart.setData(lineData);
                        lineChart.getAxisRight().setTextColor(Color.WHITE); // left y-axis
                        lineChart.getXAxis().setTextColor(Color.WHITE);
                        lineChart.getLegend().setTextColor(Color.WHITE);

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return months[(int) value - 1];
                            }

                        });

                        YAxis yAxisLeft = lineChart.getAxisLeft();
                        yAxisLeft.setEnabled(false);

                        YAxis yAxisRight = lineChart.getAxisRight();
                        yAxisRight.setGranularity(1f);

                        //bar

                        BarDataSet barDataSet = new BarDataSet(barEntries, "Total Lead");
                        barDataSet.setBarBorderWidth(0.9f);
                        barDataSet.setColors(Color.parseColor("#b41fc4"));
                        barDataSet.setBarBorderColor(Color.parseColor("#b41fc4"));
                        barDataSet.setBarShadowColor(Color.WHITE);
                        barDataSet.setValueTextColor(Color.WHITE);
                        BarData barData = new BarData(barDataSet);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                            final String[] month = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
                        XAxis xAxisBar = barChart.getXAxis();
                        xAxisBar.setGranularity(1f);
                        xAxisBar.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return months[(int) value - 1];
                            }

                        });
                        YAxis yAxisrightbar = barChart.getAxisRight();
                        yAxisrightbar.setEnabled(false);
                        barChart.setFitBars(false);
                        barChart.getDescription().setEnabled(false);
                        barChart.setData(barData);
                        barChart.animateXY(5000, 5000);
                        barChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                        barChart.getXAxis().setTextColor(Color.WHITE);
                        barChart.getLegend().setTextColor(Color.WHITE);
                        barChart.getAxisRight().setDrawAxisLine(false);

                        JSONObject lead_total = result_lead_total.getJSONObject(0);
                        Integer initial = lead_total.getInt("INITIAL");
                        Integer open = lead_total.getInt("OPEN");
                        Integer sd = lead_total.getInt("SD");
                        Integer tp = lead_total.getInt("TP");
                        Integer win = lead_total.getInt("WIN");
                        Integer lose = lead_total.getInt("LOSE");
                        tvLeadRegister.setText(String.valueOf(initial));
                        tvOpen.setText(String.valueOf(open + sd + tp));
                        tvWin.setText(String.valueOf(win));
                        tvLose.setText(String.valueOf(lose));


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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(strReq);

    }

}
