package predigsystem.udl.org.predigsystem.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Date;

import predigsystem.udl.org.predigsystem.Database.PredigAppDB;
import predigsystem.udl.org.predigsystem.Interfaces.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;

import predigsystem.udl.org.predigsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static predigsystem.udl.org.predigsystem.Fragments.MapFragment.MY_PERMISSIONS_REQUEST_LOCATION;


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    BloodPressure bloodPressure;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation;
    PredigAPIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

        bloodPressure = new BloodPressure("uid123", new Date().getTime(), 11.0, 82.3, 12.3, 7.9, 80);

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

        testNotifications();
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/predig/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PredigAPIService.class);
        service.newBloodPressureToUser(bloodPressure).enqueue(new Callback<BloodPressure>() {
            @Override
            public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                BloodPressure bP = response.body();
                if(bP != null) Toast.makeText(getApplicationContext(), R.string.bp_saved, Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), R.string.bp_not_saved, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BloodPressure> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.bp_not_saved, Toast.LENGTH_SHORT).show();
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

    private void testNotifications(){
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
                .setContentText("Take care!")
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
