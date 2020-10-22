package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Recents extends Fragment {
    private String title;
    private int page;
    private static boolean transfer = false;
    ListView articlegv;
    FirebaseDatabase rootref;
    MaterialSearchView materialSearchView;
    Button previous, suivant;
    TextView counter;

    public static Recents newInstance(int page, String title) {
        Recents recents = new Recents();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        recents.setArguments(args);
        return recents;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.recents_tab1, container, false);
        super.onCreate(savedInstanceState);
        articlegv = view.findViewById(R.id.gridview);
        // myList = Sorting.getItems(15);
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), 1, "Recents");
        articlegv.setAdapter(new ArticleAdapter(getContext(), 1, "Recents"));
        // View footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_for_recents, null, false);
        // articlegv.addFooterView(footerView);
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getContext(), articlePage.class);
                startActivity(myIntent);
            }
        });

        previous = view.findViewById(R.id.previous);
        suivant = view.findViewById(R.id.suivant);
        counter = view.findViewById(R.id.counter);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Fetching.RecentsPageNumber != 1){
                    Fetching.RecentsPageNumber--;
                    counter.setText(Integer.toString(Fetching.RecentsPageNumber));
                    articleAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Action Impossible", Toast.LENGTH_SHORT);
                }
            }
        });
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.RecentsPageNumber = Fetching.RecentsPageNumber + 1;
                counter.setText(Integer.toString(Fetching.RecentsPageNumber));
                articleAdapter.notifyDataSetChanged();
            }
        });
        /*
        articlegv.setOnItemClickListener((parent, v, position, id) -> {
            //Get to specific chosen Article page, It is not complete yet tho
            Intent myIntent = new Intent(getActivity(), articlePage.class);
            // myIntent.putExtra("id", (Parcelable) myList.get(position));
            startActivity(myIntent);
        });
        */
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





