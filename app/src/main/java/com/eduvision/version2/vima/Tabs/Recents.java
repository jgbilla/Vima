package com.eduvision.version2.vima.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.Adapters.RecentsRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter;
import com.eduvision.version2.vima.articlePage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Recents extends Fragment {
    private String title;
    private int page;
    private static boolean transfer = false;
    static RecyclerView articlegv;
    MaterialSearchView materialSearchView;
    TextView counter;

    public static ArrayList<IndividualArticle> myLikedItems = new ArrayList<>(1);

    public static ArrayList<Integer> likedItemsPosition = new ArrayList<>(1);

    static LinearLayoutManager manager;
    public static RecentsRecyclerAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.recents_tab1, container, false);
        super.onCreate(savedInstanceState);
        mAdapter = new RecentsRecyclerAdapter(Fetching.myData, getContext());
        manager = new LinearLayoutManager(getContext());
        articlegv = view.findViewById(R.id.gridview);
        articlegv.setHasFixedSize(true);
        articlegv.setLayoutManager(manager);

        articlegv.setAdapter(mAdapter);

        FloatingActionButton goBack = view.findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    articlegv.smoothScrollToPosition(0);
            }
        });

        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        rootref = FirebaseDatabase.getInstance();


        DatabaseReference articleRef = rootref.getReference("Articles");
        list = new String[]{"Clipcodes", "Android Tutorials", "Youtube Clipcodes Tutorials", "SearchView Clicodes", "Android Clipcodes", "Tutorials Clipcodes"};

       materialSearchView = (MaterialSearchView)getView().findViewById(R.id.mysearch);
        materialSearchView.clearFocus();
        materialSearchView.setSuggestions(list);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Here Create your filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //You can make change realtime if you typing here
                //See my tutorials for filtering with ListView
                return false;


            }

        });
        */
    }

}