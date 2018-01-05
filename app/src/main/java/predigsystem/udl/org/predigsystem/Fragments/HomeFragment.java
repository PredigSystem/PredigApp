package predigsystem.udl.org.predigsystem.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Calendar;
import java.util.Date;

import predigsystem.udl.org.predigsystem.Activities.BloodPressureMeasurementActivity;
import predigsystem.udl.org.predigsystem.Activities.NoDeviceBloodPressureActivity;
import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.VisitsDoctor;
import predigsystem.udl.org.predigsystem.R;
import predigsystem.udl.org.predigsystem.Utils.NetworkManager;
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
    SharedPreferences sharedpreferences;
    private final static String USER_INFO = "userInfo";


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

        updateTextLabel();

        if(nextVisit != null){
            nextVisit.enqueue(new Callback<VisitsDoctor>() {
                @Override
                public void onResponse(Call<VisitsDoctor> call, Response<VisitsDoctor> response) {
                    vd = response.body();
                    if(vd != null){
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
    }


    private void updateTextLabel(){
        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("uid", "uid123");

        if(NetworkManager.checkConnection(getContext())){
            getVisitInformationApi(user);
        }else{
            Toast.makeText(getContext(), getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
        }


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

