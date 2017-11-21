package predigsystem.udl.org.predigsystem.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Date;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;

import predigsystem.udl.org.predigsystem.R;

import static predigsystem.udl.org.predigsystem.Fragments.MapFragment.MY_PERMISSIONS_REQUEST_LOCATION;


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    BloodPressure bloodPressure;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

        bloodPressure = new BloodPressure(new Float(14.4), new Float(7.8), new Float(98),new Date());

        userLocation = new Location("userLocation"); //Default Lleida
        userLocation.setLatitude(41.6183731);
        userLocation.setLongitude(0.6024253);

        getUserLocation();

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
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Pulse, Date, Latitude, Longitude, nif) VALUES ('" +bloodPressure.getSystolic() +"', '"+bloodPressure.getDiastolic()+"', '"+bloodPressure.getPulse()+"', '"+bloodPressure.getDateTaken().getTime()+"', '"+userLocation.getLatitude()+"', '" + userLocation.getLongitude()+"', '00000000X')");
        }
    }

    public void getUserLocation () {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            userLocation = location;
                        }else{
                            Toast.makeText(getApplicationContext(), "Cannot acces to your position", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
