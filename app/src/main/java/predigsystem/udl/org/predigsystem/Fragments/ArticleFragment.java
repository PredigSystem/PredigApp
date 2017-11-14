package predigsystem.udl.org.predigsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import predigsystem.udl.org.predigsystem.Activities.NotificationSettingsActivity;
import predigsystem.udl.org.predigsystem.Fragments.ArticleFragment;
import predigsystem.udl.org.predigsystem.R;

/**
 * Created by sultan on 11/5/17.
 */

public class ArticleFragment extends Fragment {
    TextView text;

    public ArticleFragment() {
        // Required empty public constructor
    }

    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        ListView lv = (ListView) view.findViewById(R.id.listViewBlood);
        ArrayList<String> arrayBlood = new ArrayList<>();

        arrayBlood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_blood)));

        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayBlood);
        lv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getActivity().findViewById(R.id.listViewBlood);
        text = getActivity().findViewById(R.id.answer);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        text.setText(R.string.ans1);
                        break;
                    case 1:
                        text.setText(R.string.ans2);
                        break;
                    case 2:
                        text.setText(R.string.ans3);
                        break;
                    case 3:
                        text.setText(R.string.ans4);
                        break;
                    case 4:
                        text.setText(R.string.ans5);
                        break;
                    case 5:
                        text.setText(R.string.ans6);
                        break;
                    case 6:
                        text.setText(R.string.ans7);
                        break;
                    case 7:
                        text.setText(R.string.ans8);
                        break;
                    case 8:
                        text.setText(R.string.ans9);
                        break;
                    case 9:
                        text.setText(R.string.ans10);
                        break;
                }
            }
        });
    }

}