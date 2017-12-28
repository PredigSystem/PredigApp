package predigsystem.udl.org.predigsystem.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import predigsystem.udl.org.predigsystem.Activities.BTConnectionActivity;
import predigsystem.udl.org.predigsystem.Activities.BloodPressureMeasurementActivity;
import predigsystem.udl.org.predigsystem.Activities.HistoryActivity;
import predigsystem.udl.org.predigsystem.Activities.NoDeviceBloodPressureActivity;
import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.JavaClasses.VisitsDoctor;
import predigsystem.udl.org.predigsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private Call<VisitsDoctor> nextVisit = null;


    public HomeFragment() {
    }

    Calendar dateTime = Calendar.getInstance();
    private TextView text;
    SQLiteDatabase db;
    VisitsDoctor vd = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PredigAppDB predigAppDB = new PredigAppDB(getActivity(), "PredigAppDB", null, 1);
        db = predigAppDB.getReadableDatabase();

        text = (TextView) getActivity().findViewById(R.id.txt_TextDateTime);

        Button btn = getActivity().findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BloodPressureMeasurementActivity.class);
                startActivity(intent);
            }
        });

        getActivity().findViewById(R.id.btn_start_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoDeviceBloodPressureActivity.class);
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

        updateTextLabel();

        nextVisit.enqueue(new Callback<VisitsDoctor>() {
            @Override
            public void onResponse(Call<VisitsDoctor> call, Response<VisitsDoctor> response) {
                vd = response.body();
                if(vd != null){
                    //Toast.makeText(getContext(), vd.getDate().toString(), Toast.LENGTH_LONG).show();
                    printVisit();
                }
                else {
                    text.setText("No visit");
                }
            }

            @Override
            public void onFailure(Call<VisitsDoctor> call, Throwable t) {
                Toast.makeText(getContext(), R.string.api_fail, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateTextLabel(){
        getLastDate("00000000X");
        getVisitInformationApi("uid123");
        //text.setText(formatDateTime.format(dateTime.getTime()));
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

    protected void getLastDate(String nif) {
        Cursor consult = db.rawQuery("SELECT * FROM VisitsDoctor WHERE NIF='"+nif+"' LIMIT 1;", null);
        if (consult.moveToFirst()){
            text.setText("Day: "+consult.getString(1) + "- Hour: " +consult.getString(2));
        }else {text.setText("No visit");}
    }

    protected void getVisitInformationApi(String user) {
        PredigAPIService service = APIConnector.getConnectionWithGson();

        nextVisit = service.lastVisitsDoctorByUser(user);
    }

    protected void printVisit() {
        Date date = new Date(vd.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)) + "/" + String.valueOf(calendar.get(Calendar.YEAR));
        text.setText("Day: " + dateStr + " Hour: " + vd.getTime());
    }

}

