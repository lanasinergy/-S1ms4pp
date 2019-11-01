package com.example.project.simsandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class DashboardActivity extends AppCompatActivity {
    //    List<Integer> Bulan = new ArrayList<>();
    List<String> status = new ArrayList<>();
    List<Integer> warna_status = new ArrayList<>();
    BarChart barChart;
    Integer[] value_status = {81, 0, 0, 0, 0, 1};
    Integer[] bulan = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    Integer[] warna = {Color.parseColor("#fcba03"), Color.rgb(242, 86, 43), Color.rgb(4, 221, 163), Color.rgb(247, 225, 39), Color.rgb(36, 109, 24), Color.rgb(229, 20, 13)};
    private LineChart lineChart;
    TextView tvLeadRegister, tvOpen, tvWin, tvLose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvLeadRegister = findViewById(R.id.lead_register);
        tvOpen = findViewById(R.id.open);
        tvWin = findViewById(R.id.win);
        tvLose = findViewById(R.id.lose);

        loadData();

    }


    private void loadData() {
        final JSONObject jobj = new JSONObject();
        final String status_value = "null";
        try {
            jobj.put("status_value", status_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, Server.URL_pie_chart, jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("response", response.toString());
                    JSONObject jObj = response;
                    JSONArray coba = jObj.getJSONArray("total");
                    JSONArray amount_lead = jObj.getJSONArray("total_amount");
                    JSONArray total_lead = jObj.getJSONArray("total_leads");
                    JSONArray total_lead_all = jObj.getJSONArray("totals");
                    JSONArray result_lead_total = jObj.getJSONArray("results_total");
                    Log.i(String.valueOf(coba), "onResponse: ");

                    PieChart pieChart = findViewById(R.id.pieChart);
                    LineChart lineChart = findViewById(R.id.lineChart);
                    BarChart barChart = findViewById(R.id.barChart);

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
                        data.setValueTextColor(Color.DKGRAY);
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
                        lineDataSet.setCircleColor(Color.YELLOW);
                        lineDataSet.setCircleRadius(6);
                        lineDataSet.setCircleHoleRadius(3);
                        lineDataSet.setDrawHighlightIndicators(true);
                        lineDataSet.setHighLightColor(Color.RED);
                        lineDataSet.setValueTextSize(12);
                        lineDataSet.setValueTextColor(Color.DKGRAY);
                        lineDataSet.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));
                        lineDataSet.setValueTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimaryDark));
                        lineDataSet.setFillColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));

                        LineData lineData = new LineData(lineDataSet);
                        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        lineChart.animateY(1000);
                        lineChart.getXAxis().setGranularityEnabled(true);
                        lineChart.getXAxis().setGranularity(1.0f);
                        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
                        lineChart.getDescription().setEnabled(false);
                        lineChart.setData(lineData);

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return months[(int) value - 1];
                            }

                        });

                        YAxis yAxisRight = lineChart.getAxisRight();
                        yAxisRight.setEnabled(false);

                        YAxis yAxisLeft = lineChart.getAxisLeft();
                        yAxisLeft.setGranularity(1f);

                        //bar

                        BarDataSet barDataSet = new BarDataSet(barEntries, "Total Lead");
                        barDataSet.setBarBorderWidth(0.9f);
                        barDataSet.setColors(Color.BLUE);
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

                        Log.i(String.valueOf(total_lead_all.length()), "total lead: ");

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);


    }

}
