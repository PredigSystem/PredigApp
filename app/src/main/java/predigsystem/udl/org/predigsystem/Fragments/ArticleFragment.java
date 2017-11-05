package predigsystem.udl.org.predigsystem.Fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import predigsystem.udl.org.predigsystem.Fragments.ArticleFragment;
import predigsystem.udl.org.predigsystem.R;

/**
 * Created by sultan on 11/5/17.
 */

public class ArticleFragment extends Fragment {

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

}
