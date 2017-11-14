package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;

import predigsystem.udl.org.predigsystem.R;


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    BloodPressure bloodPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

        Calendar calendar = Calendar.getInstance();
        bloodPressure = new BloodPressure(new Float(16.4), new Float(13.8), calendar.getTime());

        Button btn2 = findViewById(R.id.btn_save);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeasureDataBase();
                Intent intent2 = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    protected void saveMeasureDataBase(){
        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();

        if(db != null) {
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Date, Position, nif) VALUES ('" +bloodPressure.getSystolic() +"', '"+bloodPressure.getDiastolic()+"', '"+bloodPressure.getDateTaken()+"', 'Lleida', '00000000X')");
        }
    }
}
