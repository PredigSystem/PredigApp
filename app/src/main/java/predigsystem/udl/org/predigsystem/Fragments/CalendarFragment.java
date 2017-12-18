package predigsystem.udl.org.predigsystem.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        getVisits("00000000X");
        getAPIInformation("uid123");


        nextVisit.enqueue(new Callback<VisitsDoctor>() {
            @Override
            public void onResponse(Call<VisitsDoctor> call, Response<VisitsDoctor> response) {
                VisitsDoctor vd = response.body();
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
        CalendarView calendarView = (CalendarView) getActivity().findViewById(R.id.calendar);
        calendarView.setDate(visit);
    }
}
