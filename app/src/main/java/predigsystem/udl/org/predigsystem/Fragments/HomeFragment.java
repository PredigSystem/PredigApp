package predigsystem.udl.org.predigsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import predigsystem.udl.org.predigsystem.Activities.BTConnectionActivity;
import predigsystem.udl.org.predigsystem.Activities.BloodPressureMeasurementActivity;
import predigsystem.udl.org.predigsystem.Activities.HistoryActivity;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn = getActivity().findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BloodPressureMeasurementActivity.class);
                startActivity(intent);
            }
        });

        BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_home);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                if (tabId == R.id.tab_favourites) {
                    intent = new Intent(getContext(), HistoryActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                if (tabId == R.id.tab_host) {
                    intent = new Intent(getContext(), BTConnectionActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }
}

