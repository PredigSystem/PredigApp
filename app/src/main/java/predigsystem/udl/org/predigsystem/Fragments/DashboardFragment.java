package predigsystem.udl.org.predigsystem.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


        /* LINE CHART */
        BloodPressure b1 = new BloodPressure(12.0f, 7.0f, new Date(2017, 10, 1));
        BloodPressure b2 = new BloodPressure(13.0f, 6.0f, new Date(2017, 10, 5));
        BloodPressure b3 = new BloodPressure(15.0f, 8.0f, new Date(2017, 10, 14));
        BloodPressure b4 = new BloodPressure(12.0f, 6.5f, new Date(2017, 10, 21));
        BloodPressure b5 = new BloodPressure(12.5f, 7.0f, new Date(2017, 10, 23));

        List<Entry> entryList = new ArrayList<Entry>();

        entryList.add(new Entry(b1.getDateTaken().getTime(), b1.getSystolic()));
        entryList.add(new Entry(b2.getDateTaken().getTime(), b2.getSystolic()));
        entryList.add(new Entry(b3.getDateTaken().getTime(), b3.getSystolic()));
        entryList.add(new Entry(b4.getDateTaken().getTime(), b4.getSystolic()));
        entryList.add(new Entry(b5.getDateTaken().getTime(), b5.getSystolic()));

        List<Entry> entryList2 = new ArrayList<Entry>();

        entryList2.add(new Entry(b1.getDateTaken().getTime(), b1.getDiastolic()));
        entryList2.add(new Entry(b2.getDateTaken().getTime(), b2.getDiastolic()));
        entryList2.add(new Entry(b3.getDateTaken().getTime(), b3.getDiastolic()));
        entryList2.add(new Entry(b4.getDateTaken().getTime(), b4.getDiastolic()));
        entryList2.add(new Entry(b5.getDateTaken().getTime(), b5.getDiastolic()));

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
