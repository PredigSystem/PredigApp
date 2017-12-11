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
import java.util.Date;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.Interfaces.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;

import predigsystem.udl.org.predigsystem.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static predigsystem.udl.org.predigsystem.Fragments.MapFragment.MY_PERMISSIONS_REQUEST_LOCATION;


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    BloodPressure bloodPressure;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

        bloodPressure = new BloodPressure("uid123", new java.sql.Date(new Date().getTime()), 11.0, 82.3, 12.3, 7.9, 80);

        userLocation = new Location("userLocation"); //Default Lleida
        userLocation.setLatitude(41.6183731);
        userLocation.setLongitude(0.6024253);

        getUserLocation();

        Button btn2 = findViewById(R.id.btn_save);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAPIInformation("uid123", bloodPressure);
                Intent intent2 = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    //TODO: Add local database
    /*protected void saveMeasureDataBase(){
        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();

        if(db != null) {
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Pulse, Date, Latitude, Longitude, nif) VALUES ('" +bloodPressure.getSystolic() +"', '"+bloodPressure.getDiastolic()+"', '"+bloodPressure.getPulse()+"', '"+bloodPressure.getDate().getTime()+"', '"+userLocation.getLatitude()+"', '" + userLocation.getLongitude()+"', '00000000X')");
        }
    }*/

    private void getAPIInformation(String user, BloodPressure bloodPressure){
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

        try {
            service.newBloodPressureToUser(user, bloodPressure).execute();
        } catch (IOException e) {
            Toast.makeText(this, R.string.bp_not_saved, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
