package predigsystem.udl.org.predigsystem.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import predigsystem.udl.org.predigsystem.Api.APIConnector;
import predigsystem.udl.org.predigsystem.Api.PredigAPIService;
import predigsystem.udl.org.predigsystem.JavaClasses.BloodPressure;
import predigsystem.udl.org.predigsystem.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {
    PredigAPIService service;
    SharedPreferences sharedpreferences;
    private final static String USER_INFO = "userInfo";
    String[] itemsSyst;
    String[] itemsDias;
    String[] itemsPulse;
    String[] itemsTime;

    List<String> itemListSyst = new ArrayList<>();
    List<String> itemListDiast = new ArrayList<>();
    List<String> itemListPulse = new ArrayList<>();
    List<String> itemListTime = new ArrayList<>();

    ListView sysList, diasList, pulseList, timeList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        getAPIInformation();

        sysList = view.findViewById(R.id.historicListSyst);
        diasList = view.findViewById(R.id.historicListDiast);
        pulseList = view.findViewById(R.id.historicListPulse);
        timeList = view.findViewById(R.id.historicListTime);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getAPIInformation(){
        String user = sharedpreferences.getString("uid", "uid123");

        service = APIConnector.getConnection();
        service.bloodPressureByUser(user).enqueue(new Callback<List<BloodPressure>>() {
            @Override
            public void onResponse(Call<List<BloodPressure>> call, Response<List<BloodPressure>> response) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                for(BloodPressure b: response.body()){
                    itemListSyst.add(b.getSystolic().toString());
                    itemListDiast.add(b.getDiastolic().toString());
                    itemListPulse.add(b.getPulse().toString());
                    itemListTime.add(format.format(new Date(b.getDate())));
                }

                updateLists();
            }

            @Override
            public void onFailure(Call<List<BloodPressure>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.bplist_not_found, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLists(){
        itemsSyst = new String [itemListSyst.size()];
        itemListSyst.toArray(itemsSyst);

        itemsDias = new String [itemListDiast.size()];
        itemListDiast.toArray(itemsDias);

        itemsPulse = new String [itemListPulse.size()];
        itemListPulse.toArray(itemsPulse);

        itemsTime = new String [itemListTime.size()];
        itemListTime.toArray(itemsTime);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemsSyst);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemsDias);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemsPulse);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemsTime);

        sysList.setAdapter(adapter1);
        diasList.setAdapter(adapter2);
        pulseList.setAdapter(adapter3);
        timeList.setAdapter(adapter4);
    }
}
