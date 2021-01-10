package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.Adapters.PopularRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Popular extends Fragment {
    RecyclerView grid;
    Button previous, suivant;
    TextView counter;

    public static Popular newInstance(int page, String title) {
        Popular populars = new Popular();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        populars.setArguments(args);
        return populars;
    }

    public static PopularRecyclerAdapter mAdapter;
    //TODO: Add footer to list
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.popular_tab2, container, false);
        super.onCreate(savedInstanceState);
        grid = view.findViewById(R.id.grid);
        grid.setHasFixedSize(true);
        GridLayoutManager a = new GridLayoutManager(getContext(), 2);
        grid.setLayoutManager(a);

        mAdapter = new PopularRecyclerAdapter(Fetching.myData, getContext());
        grid.setAdapter(mAdapter);

        grid.setAdapter(mAdapter);

        FloatingActionButton goBack = view.findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid.smoothScrollToPosition(0);
            }
        });


        /*
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getContext(), articlePage.class);
                startActivity(myIntent);
            }
        });

         */


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
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
