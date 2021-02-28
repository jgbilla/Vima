package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eduvision.version2.vima.Filter;
import com.eduvision.version2.vima.Home;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.Adapters.PopularRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Popular extends Fragment {
    RecyclerView grid;
    Button previous, suivant;
    TextView counter;
    int resultCodeForPopularFilter = 2;

    public static Popular newInstance(int resultCodeForPopularFilter) {
        Popular populars = new Popular();
        return populars;
    }


    public static PopularRecyclerAdapter mAdapter;
    View filter;
    TextView textNone;
    private void setDataSourceRecycler(ArrayList<IndividualArticle> myData){
        mAdapter = new PopularRecyclerAdapter(myData, getContext());
    }
    //TODO: Add footer to list
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.popular_tab2, container, false);
        super.onCreate(savedInstanceState);
        grid = view.findViewById(R.id.grid);
        textNone = view.findViewById(R.id.textNone);
        FloatingActionButton fab = view.findViewById(R.id.filterButton);
        if(resultCodeForPopularFilter == 0){
            textNone.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }
        else{
            grid.setVisibility(View.VISIBLE);

            grid.setHasFixedSize(true);
            GridLayoutManager a = new GridLayoutManager(getContext(), 2);
            grid.setLayoutManager(a);

            if(resultCodeForPopularFilter != 1) {
                setDataSourceRecycler(Home.mySortedData);
            }
            else{
                setDataSourceRecycler(Filter.myNewData);
            }
            grid.setAdapter(mAdapter);


            mAdapter.notifyDataSetChanged();

            SwipeRefreshLayout myRefreshLayout = view.findViewById(R.id.pullToRefresh);
            myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Spinning.isDataFetched = false;
                    new DownloadFilesTask().execute();

                    if (!Fetching.isInternetAvailable(getApplicationContext())) {
                        //...
                        myRefreshLayout.setRefreshing(false);
                        Fetching.makeCustomToast(getApplicationContext(), "Pas de Connexion Internet", Toast.LENGTH_SHORT);

                    } else {
                        if (!Spinning.isDataFetched) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!Spinning.isDataFetched) {
                                        //...
                                        myRefreshLayout.setRefreshing(false);
                                        Fetching.makeCustomToast(getApplicationContext(), "Réessayez", Toast.LENGTH_SHORT);
                                    } else {
                                        myRefreshLayout.setRefreshing(false);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }, 1000);
                        } else {
                            myRefreshLayout.setRefreshing(false);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), Filter.class);
                    startActivity(myIntent);
                    mAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
