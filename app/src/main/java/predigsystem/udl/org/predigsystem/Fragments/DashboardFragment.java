package predigsystem.udl.org.predigsystem.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;
import predigsystem.udl.org.predigsystem.Utils.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    SharedPreferences sharedpreferences;
    private final static String USER_INFO = "userInfo";

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

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("uid", "uid123");

        if(NetworkManager.checkConnection(getContext())){
            getAPIInformation(user);
        }else{
            Toast.makeText(getContext(), getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
        }

        if(lastBP != null && bpList != null){
            lastBP.enqueue(new Callback<BloodPressure>() {
                @Override
                public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                    BloodPressure bp = response.body();
                    if(bp != null){
                        ((TextView)getActivity().findViewById(R.id.lastSystolic)).setText("" + bp.getSystolic());
                        ((TextView)getActivity().findViewById(R.id.lastDiastolic)).setText("" + bp.getDiastolic());
                        ((TextView)getActivity().findViewById(R.id.lastPulse)).setText("" + bp.getPulse());
                    }
                }

                @Override
                public void onFailure(Call<BloodPressure> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.api_fail, Toast.LENGTH_SHORT).show();
                }
            });

            bpList.enqueue(new Callback<List<BloodPressure>>() {
                @Override
                public void onResponse(Call<List<BloodPressure>> call, Response<List<BloodPressure>> response) {
                    bloodPressureList = response.body();
                    try{
                        for(BloodPressure b: bloodPressureList){
                            if(b.getDate() != null){
                                entryList.add(new Entry(b.getDate(), b.getSystolic().floatValue()));
                                entryList2.add(new Entry(b.getDate(), b.getDiastolic().floatValue()));
                            }

                            if(isToday(b.getDate())){
                                todayBP.add(b);
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(), R.string.api_fail, Toast.LENGTH_SHORT).show();
                    }

                    buildChart();
                }

                @Override
                public void onFailure(Call<List<BloodPressure>> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.api_fail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAPIInformation(String user){
        PredigAPIService service = APIConnector.getConnectionWithGson();

        lastBP = service.lastBloodPressureByUser(user);
        bpList = service.bloodPressureByUser(user);
    }

    private boolean isToday(Long time){
        Date date = new Date(time);
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

    private void buildChart(){
        Double systolicMean = 0.0, diastolicMean = 0.0, pulseMean = 0.0;
        for(BloodPressure b: todayBP){
            systolicMean += b.getSystolic();
            diastolicMean += b.getDiastolic();
            pulseMean += b.getPulse();
        }

        if(bloodPressureList!= null && bloodPressureList.size() > 0){
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
