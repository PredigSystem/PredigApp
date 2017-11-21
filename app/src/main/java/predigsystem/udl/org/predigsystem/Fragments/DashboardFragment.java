package predigsystem.udl.org.predigsystem.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;


public class DashboardFragment extends Fragment {

    protected LineChart chart;


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

        List<BloodPressure> bpList = new ArrayList<>();

        PredigAppDB predigAppDB = new PredigAppDB(getContext(), "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM BloodPressure", null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    Float syst = cursor.getFloat(0);
                    Float dias = cursor.getFloat(1);
                    Float pulse = cursor.getFloat(2);
                    Date date = new Date(cursor.getLong(3));

                    BloodPressure b = new BloodPressure(syst, dias, pulse, date);
                    bpList.add(b);
                }
            } finally {
                cursor.close();
            }
        }

        List<Entry> entryList = new ArrayList<Entry>();
        List<Entry> entryList2 = new ArrayList<Entry>();

        BloodPressure lastBP = null;
        Date lastDate = new Date(Long.MIN_VALUE);
        List<BloodPressure> todayBP = new ArrayList<>();

        for(BloodPressure b: bpList){
            entryList.add(new Entry(b.getDateTaken().getTime(), b.getSystolic()));
            entryList2.add(new Entry(b.getDateTaken().getTime(), b.getDiastolic()));

            if(b.getDateTaken().after(lastDate)){
                lastBP = b;
                lastDate = b.getDateTaken();
            }
            if(isToday(b.getDateTaken())){
                todayBP.add(b);
            }
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

        if(lastBP != null){
            ((TextView)getActivity().findViewById(R.id.lastSystolic)).setText("" + lastBP.getSystolic());
            ((TextView)getActivity().findViewById(R.id.lastDiastolic)).setText("" + lastBP.getDiastolic());
            ((TextView)getActivity().findViewById(R.id.lastPulse)).setText("" + lastBP.getPulse());
        }

        Float systolicMean = 0f, diastolicMean = 0f, pulseMean = 0f;
        for(BloodPressure b: todayBP){
            systolicMean += b.getSystolic();
            diastolicMean += b.getDiastolic();
            pulseMean += b.getPulse();
        }

        if(bpList.size() > 0){
            systolicMean = systolicMean / bpList.size();
            diastolicMean = diastolicMean / bpList.size();
            pulseMean = pulseMean / bpList.size();

            ((TextView)getActivity().findViewById(R.id.systolicMean)).setText("" + systolicMean);
            ((TextView)getActivity().findViewById(R.id.diastolicMean)).setText("" + diastolicMean);
            ((TextView)getActivity().findViewById(R.id.pulseMean)).setText("" + pulseMean);
        }
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
}
