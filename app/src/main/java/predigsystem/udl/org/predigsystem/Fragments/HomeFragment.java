package predigsystem.udl.org.predigsystem.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.DateFormat;
import java.util.Calendar;

import predigsystem.udl.org.predigsystem.Activities.BTConnectionActivity;
import predigsystem.udl.org.predigsystem.Activities.BloodPressureMeasurementActivity;
import predigsystem.udl.org.predigsystem.Activities.HistoryActivity;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView text;
    private Button btn_date;
    private Button btn_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        text = (TextView) getActivity().findViewById(R.id.txt_TextDateTime);
        btn_date = (Button)getActivity().findViewById(R.id.btn_datePicker);
        btn_time = (Button)getActivity().findViewById(R.id.btn_timePicker);

        Button btn = getActivity().findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BloodPressureMeasurementActivity.class);
                startActivity(intent);
            }
        });

        BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_home);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                if (tabId == R.id.tab_favourites) {
                    intent = new Intent(getContext(), HistoryActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                if (tabId == R.id.tab_host) {
                    intent = new Intent(getContext(), BTConnectionActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });

        updateTextLabel();
    }

    private void updateDate(){
        new DatePickerDialog(this.getContext(), d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(this.getContext(), t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    private void updateTextLabel(){
        text.setText(formatDateTime.format(dateTime.getTime()));
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTextLabel();
        }
    };
}

