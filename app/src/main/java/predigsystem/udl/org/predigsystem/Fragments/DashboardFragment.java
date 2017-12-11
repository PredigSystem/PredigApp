package predigsystem.udl.org.predigsystem.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.Interfaces.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardFragment extends Fragment {

    protected LineChart chart;
    private Call<BloodPressure> lastBP = null;
    private Call<List<BloodPressure>> bpList;

    List<Entry> entryList = new ArrayList<Entry>();
    List<Entry> entryList2 = new ArrayList<Entry>();
    List<BloodPressure> todayBP = new ArrayList<>();
    List<BloodPressure> bloodPressureList;



    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chart = getActivity().findViewById(R.id.chart);

        getAPIInformation("uid123");
        LastBPTask lastBPTask = new LastBPTask();
        ListBPTask listBPTask = new ListBPTask();

        lastBPTask.execute();
        listBPTask.execute();
    }

    private void getAPIInformation(String user){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.sql.Date.class, new JsonDeserializer<java.sql.Date>() {
            @Override
            public java.sql.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return new java.sql.Date(df.parse(json.getAsString()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/predig/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        PredigAPIService service = retrofit.create(PredigAPIService.class);

        lastBP = service.lastBloodPressureByUser(user);
        bpList = service.bloodPressureByUser(user);
    }

    private boolean isToday(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(new Date());
        int todayYear = cal.get(Calendar.YEAR);
        int todayMonth = cal.get(Calendar.MONTH);
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        return year == todayYear && month == todayMonth && day == todayDay;
    }

    private class LastBPTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                BloodPressure bp = lastBP.execute().body();
                if(bp != null){
                    ((TextView)getActivity().findViewById(R.id.lastSystolic)).setText("" + bp.getSystolic());
                    ((TextView)getActivity().findViewById(R.id.lastDiastolic)).setText("" + bp.getDiastolic());
                    ((TextView)getActivity().findViewById(R.id.lastPulse)).setText("" + bp.getPulse());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ListBPTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                bloodPressureList = bpList.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                for(BloodPressure b: bloodPressureList){
                    if(b.getDate() != null){
                        entryList.add(new Entry(b.getDate().getTime(), b.getSystolic().floatValue()));
                        entryList2.add(new Entry(b.getDate().getTime(), b.getDiastolic().floatValue()));
                    }

                    if(isToday(b.getDate())){
                        todayBP.add(b);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Double systolicMean = 0.0, diastolicMean = 0.0, pulseMean = 0.0;
            for(BloodPressure b: todayBP){
                systolicMean += b.getSystolic();
                diastolicMean += b.getDiastolic();
                pulseMean += b.getPulse();
            }

            if(bloodPressureList.size() > 0){
                systolicMean = systolicMean / bloodPressureList.size();
                diastolicMean = diastolicMean / bloodPressureList.size();
                pulseMean = pulseMean / bloodPressureList.size();

                ((TextView)getActivity().findViewById(R.id.systolicMean)).setText("" + systolicMean);
                ((TextView)getActivity().findViewById(R.id.diastolicMean)).setText("" + diastolicMean);
                ((TextView)getActivity().findViewById(R.id.pulseMean)).setText("" + pulseMean);
            }

            LineDataSet setComp1 = new LineDataSet(entryList, "Systolic");
            setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
            setComp1.setColor(R.color.accent);
            LineDataSet setComp2 = new LineDataSet(entryList2, "Diastolic");
            setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setComp1);
            dataSets.add(setComp2);

            LineData data = new LineData(dataSets);
            chart.setData(data);
            chart.invalidate();
        }
    }
}
