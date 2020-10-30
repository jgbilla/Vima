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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.articlePage;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Recents extends Fragment {
    private String title;
    private int page;
    private static boolean transfer = false;
    ListView articlegv;
    MaterialSearchView materialSearchView;
    Button previous, suivant;
    TextView counter;

    public static ArrayList<IndividualArticle> myLikedItems = new ArrayList<>(1);

    public static ArrayList<Integer> likedItemsPosition = new ArrayList<>(1);

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
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), 1, "Recents");
        articlegv.setAdapter(articleAdapter);

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
                    articlegv.smoothScrollToPosition(0);
                }
                else{
                    Fetching.makeCustomToast(getApplicationContext(), "Action Impossible", Toast.LENGTH_SHORT);
                }
            }
        });
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Fetching.RecentsPageNumber <= 5) {
                    Fetching.RecentsPageNumber ++;
                    counter.setText(Integer.toString(Fetching.RecentsPageNumber));
                    articleAdapter.notifyDataSetChanged();
                    articlegv.smoothScrollToPosition(0);
                }
                else {
                    Fetching.makeCustomToast(getApplicationContext(), "Action Impossible", Toast.LENGTH_SHORT);
                }
            }
        });

        SwipeRefreshLayout myRefreshLayout = view.findViewById(R.id.pullToRefresh);
        myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fetching.isDataFetched = "No";
                new DownloadFilesTask().execute();

                if (!Fetching.isInternetAvailable(getApplicationContext())) {
                    //...
                    myRefreshLayout.setRefreshing(false);
                    Fetching.makeCustomToast(getApplicationContext(), "Pas de Connexion Internet", Toast.LENGTH_SHORT);

                } else {
                    if (Fetching.isDataFetched.equals("No")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Fetching.isDataFetched.equals("No")) {
                                    //...
                                    myRefreshLayout.setRefreshing(false);
                                    Fetching.makeCustomToast(getApplicationContext(), "Réessayez", Toast.LENGTH_SHORT);
                                } else {
                                    myRefreshLayout.setRefreshing(false);
                                    articleAdapter.notifyDataSetChanged();
                                }
                            }
                        }, 1000);
                    } else {
                        myRefreshLayout.setRefreshing(false);
                        articleAdapter.notifyDataSetChanged();
                    }
                }
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