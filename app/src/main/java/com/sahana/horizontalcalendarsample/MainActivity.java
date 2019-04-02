package com.sahana.horizontalcalendarsample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sahana.horizontalcalendarview.CustomHorizontalCalendar;
import com.sahana.horizontalcalendarview.OnHorizontalDateSelectListener;
import com.sahana.horizontalcalendarview.model.DateModel;

public class MainActivity extends AppCompatActivity implements OnHorizontalDateSelectListener {
    private CustomHorizontalCalendar mCustomHorizontalCalendar;
    private TextView mDateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomHorizontalCalendar = findViewById(R.id.customHorizontal);
        mCustomHorizontalCalendar.setOnDateSelectListener(this);
        mDateTextView = findViewById(R.id.dateTextView);
    }

    @Override
    public void onDateClick(DateModel dateModel) {
        Log.d("date", dateModel != null ? dateModel.month + dateModel.day + dateModel.dayOfWeek + dateModel.year : "");
        mDateTextView.setText(dateModel != null ? dateModel.day + "," + dateModel.dayOfWeek + "," + dateModel.month + "," + dateModel.year : "");
    }
}
