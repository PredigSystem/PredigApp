package predigsystem.udl.org.predigsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import predigsystem.udl.org.predigsystem.R;

/**
 * Created by sultan on 11/16/17.
 */

public class LocationActivity extends PreferenceActivity {

    private static final int RESULT_SETTINGS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;

        }
    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

    builder.append("\n Change the view on the map: "
            + sharedPrefs.getString("prefRadiusLocation", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.locationSettings);

        settingsTextView.setText(builder.toString());
    }
}

