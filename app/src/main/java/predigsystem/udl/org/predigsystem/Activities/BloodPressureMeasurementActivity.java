package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.R;

import predigsystem.udl.org.predigsystem.R;


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

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
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Date, Position, nif) VALUES (16.4, 13.8, '13/11/2017', 'Lleida', '00000000X')");
        }
    }
}
