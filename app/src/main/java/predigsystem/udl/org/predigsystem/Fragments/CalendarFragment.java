package predigsystem.udl.org.predigsystem.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.data.CalendarDay;
import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.VisitsDoctor;
import predigsystem.udl.org.predigsystem.R;
import predigsystem.udl.org.predigsystem.Utils.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment{

    SQLiteDatabase db;
    private Call<VisitsDoctor> nextVisit = null;
    private Call<List<VisitsDoctor>> visitsList;
    private VisitsDoctor vd = null;
    CalendarView calendarView = null;
    SharedPreferences sharedpreferences;
    private final static String USER_INFO = "userInfo";


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PredigAppDB predigAppDB = new PredigAppDB(getActivity(), "PredigAppDB", null, 1);
        db = predigAppDB.getReadableDatabase();

        calendarView = (CalendarView) getActivity().findViewById(R.id.calendar);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("uid", "uid123");

        if(NetworkManager.checkConnection(getContext())){
            getAPIInformation(user);
        }else{
            Toast.makeText(getContext(), getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
        }

        //getVisits("00000000X");

        if(nextVisit != null){
            nextVisit.enqueue(new Callback<VisitsDoctor>() {
                @Override
                public void onResponse(Call<VisitsDoctor> call, Response<VisitsDoctor> response) {
                    vd = response.body();
                    if(vd != null){
                        //Toast.makeText(getContext(), vd.getDate().toString(), Toast.LENGTH_LONG).show();
                        addVisit(vd.getDate());
                    }
                    else {
                    }
                }

                @Override
                public void onFailure(Call<VisitsDoctor> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.api_fail, Toast.LENGTH_SHORT).show();
                }
            });
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date date = new Date(year-1900, month, dayOfMonth);
                showDate(date.toString());
            }
        });
    }

    public void getVisits (String nif) {
        Cursor consult = db.rawQuery("SELECT Date, Time FROM VisitsDoctor WHERE NIF='"+nif+"';", null);

        if(consult.moveToFirst())
            do {
                String date = consult.getString(0);
                String time = consult.getString(1);
            }while(consult.moveToNext());
    }

    private void getAPIInformation(String user){
        PredigAPIService service = APIConnector.getConnectionWithGson();

        nextVisit = service.lastVisitsDoctorByUser(user);
        visitsList = service.visitsDoctorByUser(user);
    }

    public void addVisit(Long visit) {
        calendarView.setDate(visit);
        //Date date = new Date(visit);
    }

    public void showDate (String date) {

        String userDate = new Date(vd.getDate()).toString();
        if(date.equals(userDate))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("Next visit");

            builder.setMessage(getString(R.string.doctor) + ": " + vd.getDoctor() + "\n" + getString(R.string.hour) + ": " + vd.getTime() + "\n" + getString(R.string.reason) +": " + vd.getReason() );

            builder.setPositiveButton("Return",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
