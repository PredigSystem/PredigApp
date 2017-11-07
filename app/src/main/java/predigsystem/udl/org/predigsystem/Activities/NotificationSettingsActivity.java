package predigsystem.udl.org.predigsystem.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import predigsystem.udl.org.predigsystem.R;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.fragment_notifications);
    }
}
