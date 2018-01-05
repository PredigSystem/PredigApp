package predigsystem.udl.org.predigsystem.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ihealth.communication.control.Bp5Control;
import com.ihealth.communication.control.BpProfile;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.ihealth.communication.manager.iHealthDevicesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

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


public class BloodPressureMeasurementActivity extends AppCompatActivity {

    TextView systolic, diastolic, pulse, title;
    Button btnSave;

    BloodPressure bloodPressure;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location userLocation = null;
    PredigAPIService service;

    private final static String USER_INFO = "userInfo";
    private static final int BLUETOOTH_ENABLED = 1;
    private static final String selectedDeviceType = "BP5";
    private static final int REQUEST_PERMISSIONS = 0;
    private static final String TAG = "BP_TAG";
    private static final String userName = "pbalaguer19@gmail.com";
    private static final String clientId = "274148308bb64d2db1153a3dddd85476";
    private static final String clientSecret = "56214c8ecd8f4d85905ef56d8bc37206";
    private Boolean bluetoothReady = false;
    private Bp5Control bp5Control;
    private String deviceMac = null;
    private String devType = null;
    private int callbackId;
    private boolean request = false;
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);

        title = findViewById(R.id.titleResult);
        systolic = findViewById(R.id.systolicResult);
        diastolic = findViewById(R.id.diastolicResult);
        pulse = findViewById(R.id.pulseResult);
        btnSave = findViewById(R.id.btn_save);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        setLoadingValues();

        checkBluetooth();
        checkPermissions();

        iHealthDevicesManager.getInstance().init(this, 2, 2);
        callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
        iHealthDevicesManager.getInstance().sdkUserInAuthor(BloodPressureMeasurementActivity.this, userName, clientId, clientSecret, callbackId);
        iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(callbackId, iHealthDevicesManager.TYPE_BP5);


        if(bluetoothReady) {
            long type = iHealthDevicesManager.DISCOVERY_BP5;
            iHealthDevicesManager.getInstance().startDiscovery(type);
        } else
            failedValues("Bluetooth is not ready!");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bloodPressure == null){
                    failedValues(getString(R.string.bp_not_saved));
                }
                if(NetworkManager.checkConnection(getApplicationContext())){
                    getAPIInformation(bloodPressure);
                }else{
                    saveMeasureDataBase();
                }
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent2);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iHealthDevicesManager.getInstance().unRegisterClientCallback(callbackId);
    }

    protected void saveMeasureDataBase(){
        PredigAppDB predigAppDB = new PredigAppDB(this, "PredigAppDB", null, 1);
        SQLiteDatabase db = predigAppDB.getWritableDatabase();

        if(db != null) {
            String user = sharedpreferences.getString("uid", "uid123");
            db.execSQL("INSERT INTO BloodPressure (Systolic, Diastolic, Pulse, Date, Latitude, Longitude, nif) VALUES ('" +bloodPressure.getSystolic() +"', '"+bloodPressure.getDiastolic()+"', '"+bloodPressure.getPulse()+"', '"+bloodPressure.getDate()+"', '"+userLocation.getLatitude()+"', '" + userLocation.getLongitude()+"', '" + user+ ")");
        }
    }

    private void setLoadingValues(){
        title.setText("Loading...");
        systolic.setText("?");
        diastolic.setText("?");
        pulse.setText("?");

        btnSave.setEnabled(false);
    }

    private void setValues(String sys, String dias, String puls){
        title.setText("Results");
        systolic.setText(sys);
        diastolic.setText(dias);
        pulse.setText(puls);
        btnSave.setEnabled(true);

        userLocation = new Location("userLocation"); //Default Lleida
        userLocation.setLatitude(41.6183731);
        userLocation.setLongitude(0.6024253);
        getUserLocation();

        Double lat = null, lon = null;

        if(userLocation != null){
            lat = userLocation.getLatitude();
            lon = userLocation.getLongitude();
        }

        String user = sharedpreferences.getString("uid", "uid123");
        bloodPressure = new BloodPressure(user, new Date().getTime(), lat, lon, Double.parseDouble(sys) / 10, Double.parseDouble(dias) / 10, Integer.parseInt(puls));
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message", true))
            testNotifications(Double.parseDouble(sys), Double.parseDouble(dias));
    }

    private void failedValues(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getAPIInformation(BloodPressure bloodPressure){
        service = APIConnector.getConnection();
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


    /*** Device functions ***/

    private void connect(){
        if(deviceMac == null || devType == null){
            deviceMac = sharedpreferences.getString("mac","");
            devType = sharedpreferences.getString("dType","");
        }
        int tries = 0;
        do {
            request = iHealthDevicesManager.getInstance().connectDevice(userName, deviceMac, devType);
            tries ++;
        }while (!request && tries < 1000000);

        if(request){
            do {
                bp5Control = iHealthDevicesManager.getInstance().getBp5Control(deviceMac);
            }while (bp5Control == null);
        }

        if(request && bp5Control != null)
            bp5Control.startMeasure();
        else
            failedValues("Cannot connect to the Blood Pressure device!");
    }

    private com.ihealth.communication.manager.iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

        @Override
        public void onScanDevice(String mac, String deviceType, int rssi) {
            super.onScanDevice(mac, deviceType, rssi);
            onScanDevice(mac, deviceType);
        }

        @Override
        public void onScanDevice(String mac, String deviceType, int rssi, Map manufactorData) {
            super.onScanDevice(mac, deviceType, rssi, manufactorData);
            onScanDevice(mac, deviceType);
        }

        private void onScanDevice(String mac, String deviceType){
            deviceMac = mac;
            devType = deviceType;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("mac", mac);
            editor.putString("dType", deviceType);
            editor.apply();
        }

        @Override
        public void onUserStatus(String username, int userStatus) {}

        //TODO: Change things, add Toasts!!!
        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {
            Log.i(TAG, "mac: " + mac);
            Log.i(TAG, "deviceType: " + deviceType);
            Log.i(TAG, "action: " + action);
            Log.i(TAG, "message: " + message);

            if(BpProfile.ACTION_BATTERY_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String battery = info.getString(BpProfile.BATTERY_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "battery: " + battery;
                    myHandler.sendMessage(msg);

                    failedValues("Device battery low!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else if(BpProfile.ACTION_DISENABLE_OFFLINE_BP.equals(action)){
                Log.i(TAG, "disable operation is success");

            }else if(BpProfile.ACTION_ENABLE_OFFLINE_BP.equals(action)){
                Log.i(TAG, "enable operation is success");

            }else if(BpProfile.ACTION_ERROR_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String num =info.getString(BpProfile.ERROR_NUM_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "error num: " + num;
                    myHandler.sendMessage(msg);

                    failedValues("An error occurred");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_HISTORICAL_DATA_BP.equals(action)){
                String str = "";
                try {
                    JSONObject info = new JSONObject(message);
                    if (info.has(BpProfile.HISTORICAL_DATA_BP)) {
                        JSONArray array = info.getJSONArray(BpProfile.HISTORICAL_DATA_BP);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String date          = obj.getString(BpProfile.MEASUREMENT_DATE_BP);
                            String hightPressure = obj.getString(BpProfile.HIGH_BLOOD_PRESSURE_BP);
                            String lowPressure   = obj.getString(BpProfile.LOW_BLOOD_PRESSURE_BP);
                            String pulseWave     = obj.getString(BpProfile.PULSE_BP);
                            String ahr           = obj.getString(BpProfile.MEASUREMENT_AHR_BP);
                            String hsd           = obj.getString(BpProfile.MEASUREMENT_HSD_BP);
                            str = "date:" + date
                                    + "hightPressure:" + hightPressure + "\n"
                                    + "lowPressure:" + lowPressure + "\n"
                                    + "pulseWave" + pulseWave + "\n"
                                    + "ahr:" + ahr + "\n"
                                    + "hsd:" + hsd + "\n";
                        }
                    }
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj =  str;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_HISTORICAL_NUM_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String num = info.getString(BpProfile.HISTORICAL_NUM_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "num: " + num;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_IS_ENABLE_OFFLINE.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String isEnableoffline =info.getString(BpProfile.IS_ENABLE_OFFLINE);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "isEnableoffline: " + isEnableoffline;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_ONLINE_PRESSURE_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String pressure =info.getString(BpProfile.BLOOD_PRESSURE_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "pressure: " + pressure;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_ONLINE_PULSEWAVE_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String pressure =info.getString(BpProfile.BLOOD_PRESSURE_BP);
                    String wave = info.getString(BpProfile.PULSEWAVE_BP);
                    String heartbeat = info.getString(BpProfile.FLAG_HEARTBEAT_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "pressure:" + pressure + "\n"
                            + "wave: " + wave + "\n"
                            + " - heartbeat:" + heartbeat;
                    myHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_ONLINE_RESULT_BP.equals(action)){
                try {
                    JSONObject info = new JSONObject(message);
                    String highPressure =info.getString(BpProfile.HIGH_BLOOD_PRESSURE_BP);
                    String lowPressure =info.getString(BpProfile.LOW_BLOOD_PRESSURE_BP);
                    String ahr =info.getString(BpProfile.MEASUREMENT_AHR_BP);
                    String pulse =info.getString(BpProfile.PULSE_BP);
                    Message msg = new Message();
                    msg.what = HANDLER_MESSAGE;
                    msg.obj = "highPressure: " + highPressure
                            + "lowPressure: " + lowPressure
                            + "ahr: " + ahr
                            + "pulse: " + pulse;
                    myHandler.sendMessage(msg);

                    setValues(highPressure, lowPressure, pulse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(BpProfile.ACTION_ZOREING_BP.equals(action)){
                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "zoreing";
                myHandler.sendMessage(msg);

            }else if(BpProfile.ACTION_ZOREOVER_BP.equals(action)){
                Message msg = new Message();
                msg.what = HANDLER_MESSAGE;
                msg.obj = "zoreover";
                myHandler.sendMessage(msg);

            }

        }

        @Override
        public void onScanFinish() {
            connect();
        }
    };

    private void checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enable
            Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,BLUETOOTH_ENABLED);
        } else {
            bluetoothReady = true;
        }
    }

    private void checkPermissions() {
        StringBuilder tempRequest = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tempRequest.append(Manifest.permission.WRITE_EXTERNAL_STORAGE + ",");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            tempRequest.append(Manifest.permission.RECORD_AUDIO + ",");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tempRequest.append(Manifest.permission.ACCESS_FINE_LOCATION + ",");
        }
        if (tempRequest.length() > 0) {
            tempRequest.deleteCharAt(tempRequest.length() - 1);
            ActivityCompat.requestPermissions(this, tempRequest.toString().split(","), REQUEST_PERMISSIONS);
        }
    }

    private static final int HANDLER_MESSAGE = 101;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE:
                    //tv_return.setText((String)msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
