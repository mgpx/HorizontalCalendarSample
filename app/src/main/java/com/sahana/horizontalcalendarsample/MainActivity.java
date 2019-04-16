package com.sahana.horizontalcalendarsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sahana.horizontalcalendarview.CustomHorizontalCalendar;
import com.sahana.horizontalcalendarview.OnHorizontalDateSelectListener;
import com.sahana.horizontalcalendarview.model.DateModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnHorizontalDateSelectListener {
    private CustomHorizontalCalendar mCustomHorizontalCalendar;
    private TextView mDateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomHorizontalCalendar = findViewById(R.id.customHorizontal);
        mCustomHorizontalCalendar.setOnDateSelectListener(this);
        mCustomHorizontalCalendar.setLocale(Locale.getDefault());
        mDateTextView = findViewById(R.id.dateTextView);

        findViewById(R.id.btnEnglish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomHorizontalCalendar.setLocale(Locale.ENGLISH);
            }
        });

        findViewById(R.id.btnPortuguese).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomHorizontalCalendar.setLocale(new Locale("pt", "BR"));
            }
        });
    }

    @Override
    public void onDateClick(DateModel dateModel) {
        Log.d("date", dateModel != null ? dateModel.month + dateModel.day + dateModel.dayOfWeek + dateModel.year : "");
        mDateTextView.setText(dateModel != null ? dateModel.day + "," + dateModel.dayOfWeek + "," + dateModel.month + "," + dateModel.year : "");
    }
}
