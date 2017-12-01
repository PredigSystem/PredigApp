package predigsystem.udl.org.predigsystem.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import predigsystem.udl.org.predigsystem.Adapters.FAQAdapter;
import predigsystem.udl.org.predigsystem.JavaClasses.FAQ;
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
        List<String> answersFaq = new ArrayList<>();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<FAQ> data = new ArrayList<>();

        arrayBlood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_blood)));
        answersFaq.addAll(Arrays.asList(getResources().getStringArray(R.array.answers_faq)));

        for(int i = 0; i < arrayBlood.size(); i++){
            data.add(new FAQ(
                    arrayBlood.get(i),
                    answersFaq.get(i),
                    R.color.white,
                    R.color.white_2,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        recyclerView.setAdapter(new FAQAdapter(data));

        return view;
    }
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}