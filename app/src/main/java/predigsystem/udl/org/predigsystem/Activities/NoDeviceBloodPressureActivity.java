package predigsystem.udl.org.predigsystem.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;
import predigsystem.udl.org.predigsystem.Utils.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static predigsystem.udl.org.predigsystem.Fragments.MapFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class NoDeviceBloodPressureActivity extends AppCompatActivity implements View.OnClickListener{
    private Button save;
    private EditText systolic, diastolic, pulse;

    PredigAPIService service;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation = null;

    private final static String USER_INFO = "userInfo";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_device_blood_pressure);

        save = findViewById(R.id.btn_save);
        save.setOnClickListener(this);

        systolic = findViewById(R.id.systolicEditText);
        diastolic = findViewById(R.id.diastolicEditText);
        pulse = findViewById(R.id.pulseEditText);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        if(canBeSaved()){
            saveValues();
        }else{
            Toast.makeText(this, "Enter all the values", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canBeSaved(){
        return systolic.getText().length() > 0 &&
                diastolic.getText().length() > 0 &&
                pulse.getText().length() > 0;
    }

    private void saveValues(){
        userLocation = new Location("userLocation"); //Default Lleida
        userLocation.setLatitude(41.6183731);
        userLocation.setLongitude(0.6024253);
        getUserLocation();

        Double lat = null, lon = null;

        if(userLocation != null){
            lat = userLocation.getLatitude();
            lon = userLocation.getLongitude();
        }

        Double syst = Double.parseDouble(systolic.getText().toString());
        Double diast = Double.parseDouble(diastolic.getText().toString());
        Integer puls = Integer.parseInt(pulse.getText().toString());

        //Covert to mmHg units (i.e. 123 -> 12.3)
        while(syst > 100) syst = syst / 10;
        while(diast > 100) diast = diast / 10;

        String user = sharedpreferences.getString("uid", "uid123");
        BloodPressure bloodPressure = new BloodPressure(user, new Date().getTime(), lat, lon, syst, diast, puls);
        testNotifications(bloodPressure.getSystolic(), bloodPressure.getDiastolic());

        if(NetworkManager.checkConnection(getApplicationContext())){
            getAPIInformation(bloodPressure);
        }else{
            saveMeasureDataBase(bloodPressure);
        }
    }

    protected void saveMeasureDataBase(BloodPressure bloodPressure){
        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();

        if(db != null) {
            String user = sharedpreferences.getString("uid", "uid123");
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Pulse, Date, Latitude, Longitude, nif) VALUES ('" +bloodPressure.getSystolic() +"', '"+bloodPressure.getDiastolic()+"', '"+bloodPressure.getPulse()+"', '"+bloodPressure.getDate()+"', '"+userLocation.getLatitude()+"', '" + userLocation.getLongitude()+"', '" + user+ ")");
        }
    }

    private void getAPIInformation(BloodPressure bloodPressure){
        service = APIConnector.getConnection();
        service.newBloodPressureToUser(bloodPressure).enqueue(new Callback<BloodPressure>() {
            @Override
            public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                BloodPressure bP = response.body();
                if(bP != null) Toast.makeText(getApplicationContext(), R.string.bp_saved, Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), R.string.bp_not_saved, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<BloodPressure> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.bp_not_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

    private void testNotifications(Double syst, Double diast){
        boolean high = false;
        String type = "";
        if(syst > 130 || diast >= 80){
            high = true;
            type = "Stage 1";
        }
        if(syst > 140 || diast >= 90) type = "Stage 2";
        if(syst > 180 || diast >= 120) type = "Hypertensive crisis";


        if(high){
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = "channel";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            }

            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // this is a my insertion looking for a solution
            int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.logo: R.mipmap.ic_launcher;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(icon)
                    .setContentTitle("HIGH Blood pressure!")
                    .setContentText("Type: " + type)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(mChannel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

}
