package com.example.project.simsandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class coba_coba extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;

    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_coba);

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        textView = findViewById(R.id.tvSwipe);

        textView.setText("Total number = 0");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                number++;
                textView.setText("Total number = " + number);
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

    }
}
