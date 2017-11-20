package predigsystem.udl.org.predigsystem.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import predigsystem.udl.org.predigsystem.Activities.LocationActivity;
import predigsystem.udl.org.predigsystem.Activities.NotificationSettingsActivity;
import predigsystem.udl.org.predigsystem.Activities.UserSettingsActivity;
import predigsystem.udl.org.predigsystem.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getActivity().findViewById(R.id.listView_settings);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(getActivity(), UserSettingsActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent3 = new Intent(getActivity(), LocationActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });
    }
}
