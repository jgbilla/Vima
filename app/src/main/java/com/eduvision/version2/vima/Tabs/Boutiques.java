package com.eduvision.version2.vima.Tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.Adapters.BoutiquesRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Adapters.BoutiquesRecyclerAdapterLandscape;
import com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Boutiques#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Boutiques extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Boutiques() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Boutiques.
     */
    // TODO: Rename and change types and number of parameters
    public static Boutiques newInstance(String param1, String param2) {
        Boutiques fragment = new Boutiques();
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

        //int gridColumnCount =
        getResources().getInteger(R.integer.grid_column_count);

        RecyclerView recycle = getView().findViewById(R.id.recyclerView);
        recycle.setHasFixedSize(true);

        if (getActivity().getResources().getConfiguration().orientation == 1) { //returns 1 if portrait and 2 if landscape
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycle.setLayoutManager(layoutManager);
            BoutiquesRecyclerAdapter mAdapter = new BoutiquesRecyclerAdapter(Spinning.myData, getContext());
            recycle.setAdapter(mAdapter);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recycle.setLayoutManager(layoutManager);
            BoutiquesRecyclerAdapterLandscape mAdapter = new BoutiquesRecyclerAdapterLandscape(Spinning.myData, getContext());
            recycle.setAdapter(mAdapter);
        }
    }
}