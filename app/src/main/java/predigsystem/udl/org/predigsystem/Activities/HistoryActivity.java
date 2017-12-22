package predigsystem.udl.org.predigsystem.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;

public class HistoryActivity extends AppCompatActivity {

        ListView simpleList;
        String List1[] = {"11.30    8.00    84    23/11/2017", "14.40    7.80    98    21/11/2017", "12.50    8.20    91    20/11/2017", "13.60    8.40    88    18/11/2017", "15.70    7.60    95    18/11/2017"};


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_history);

                simpleList = (ListView)findViewById(R.id.measurementlist);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, List1);
                simpleList.setAdapter(arrayAdapter);

                //bloodPressure = new BloodPressure(new Float(14.4), new Float(7.8), new Float(98), new Date());

                BottomBar bottomBar = findViewById(R.id.bottomBar);
                bottomBar.setDefaultTab(R.id.tab_favourites);
                bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                        @Override
                        public void onTabSelected(@IdRes int tabId) {
                                Intent intent;
                                if (tabId == R.id.tab_home) {
                                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                }
                                if (tabId == R.id.tab_host) {
                                        intent = new Intent(getApplicationContext(), BTConnectionActivity.class);
                                        startActivity(intent);
                                        finish();
                                }
                        }
                });

                List<BloodPressure> bpList = new ArrayList<>();

                PredigAppDB predigAppDB = new PredigAppDB(getBaseContext(), "PredigAppDB", null, 1);
                SQLiteDatabase db = predigAppDB.getWritableDatabase();
                final Cursor cursor = db.rawQuery("SELECT * FROM BloodPressure", null);

                if (cursor != null) {
                        try {
                                if (cursor.moveToFirst()) {
                                        Float syst = cursor.getFloat(0);
                                        Float dias = cursor.getFloat(1);
                                        Float pulse = cursor.getFloat(2);
                                        Date date = new Date(cursor.getLong(3));

                                        //BloodPressure b = new BloodPressure(syst, dias, pulse, date);
                                        //bpList.add(b);
                                }
                        } finally {
                                cursor.close();
                        }
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
