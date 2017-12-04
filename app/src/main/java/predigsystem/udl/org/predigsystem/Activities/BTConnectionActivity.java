package predigsystem.udl.org.predigsystem.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ihealth.communication.manager.iHealthDevicesManager;
import com.ihealth.communication.manager.iHealthDevicesCallback;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import predigsystem.udl.org.predigsystem.R;

public class BTConnectionActivity extends AppCompatActivity {

    private final static int BLUETOOTH_ENABLED = 1;
    private String userName = "pbalaguer19@gmail.com";
    private String clientId = "274148308bb64d2db1153a3dddd85476";
    private String clientSecret = "56214c8ecd8f4d85905ef56d8bc37206";
    private Boolean bluetoothReady = false;
    private ProgressDialog progressDialog;
    private Class selectedDeviceClass;
    private String selectedDeviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnection);

        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_host);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                if (tabId == R.id.tab_favourites) {
                    intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (tabId == R.id.tab_home) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        checkBluetooth();
        iHealthDevicesManager.getInstance().init(BTConnectionActivity.this);
        int callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
        iHealthDevicesManager.getInstance().sdkUserInAuthor(BTConnectionActivity.this, userName, clientId, clientSecret, callbackId);

        if(bluetoothReady) {
            int type = iHealthDevicesManager.DISCOVERY_BP5;
            iHealthDevicesManager.getInstance().startDiscovery(type);
        } else {
            Toast.makeText(this,"Bluetooth is not ready!", Toast.LENGTH_SHORT).show();
        }
    }

    private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

        @Override
        public void onScanDevice(String mac, String deviceType) {
            /*if (selectedDeviceType.equals(deviceType)) {
                iHealthDevicesManager.getInstance().stopDiscovery();
                iHealthDevicesManager.getInstance().connectDevice(userName, mac);
            }*/
        }

        @Override
        public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
            /*if (selectedDeviceType.equals(deviceType) && status==1) {
                Intent intent = new Intent();
                intent.putExtra("mac", mac);
                intent.setClass(MainActivity.this, selectedDeviceClass);
                startActivityForResult(intent, DEVICE);
            }*/
        }

        @Override
        public void onUserStatus(String username, int userStatus) {}

        @Override
        public void onDeviceNotify(String mac, String deviceType, String action, String message) {}

        @Override
        public void onScanFinish() {
            //progressDialog.dismiss();
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
}
