package com.eduvision.version2.vima.BoutiquesTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;

import java.util.Objects;


public class tab_boutiques extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public tab_boutiques() {
        // Required empty public constructor
    }

    public static tab_boutiques newInstance(String param1, String param2) {
        tab_boutiques fragment = new tab_boutiques();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_boutiques, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getResources().getInteger(R.integer.grid_column_count);
        ListView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.list);
        recyclerView.setAdapter(new ArticleAdapter(getContext(), 1, "Boutiques"));

    }

}