package com.eduvision.version2.vima;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.SearchEngine.Search_engine;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Verify;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;

public class Home extends Fragment {
    private static final int REQUEST_LOCATION = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<IndividualArticle> mySortedData = new ArrayList<>(80);
    TextView sRecents, sPop, sShop;
   ProgressBar Progress;
    ImageView featured, recents1, recents2, recents3, pop1, pop2, pop3, shop1, shop2, shop3, profile;
    EditText searchView;
    RecyclerView searchResults;
    DatabaseReference databaseReference;
    Button clear;
    ArrayList<String> nameList;
    ArrayList<StorageReference> photoList;
    Search_engine search = new Search_engine();
    private int mParam1;
    private int mParam2;


    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(int param1, int param2) {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);

        Home fragment = new Home();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
}

public ArrayList<IndividualArticle> sort(ArrayList<IndividualArticle> myList1){
        ArrayList<IndividualArticle> myList = myList1;
        for(int i = 0; i<(myList.size()-1); i++) {
            for (int a = 0; a < (myList.size()-1); a++) {
                if (myList.get(a).getPopularity_index() > myList.get(a + 1).getPopularity_index()) {
                    IndividualArticle temp = myList.get(a + 1);
                    myList.set(a + 1, myList.get(a));
                    myList.set(a, temp);
                }
            }
        }
        return myList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new DownloadFilesTask().execute();
        mySortedData = sort(Fetching.myData);
        if (mySortedData.isEmpty()){
            new DownloadFilesTask().execute();
        }

        else{

            Log.println(Log.INFO,"Tagging", String.valueOf(FetchShops.shopData.size()));
            sRecents = view.findViewById(R.id.see_recents);
            sPop = view.findViewById(R.id.see_popular);
            sShop = view.findViewById(R.id.see_shops);

            shop1 = view.findViewById(R.id.fsimage);
            shop2 = view.findViewById(R.id.ssimage);
            shop3 = view.findViewById(R.id.tsimage);

            featured = view.findViewById(R.id.featured);

            recents1 = view.findViewById(R.id.frimage); //fr = first recent
            recents2 = view.findViewById(R.id.srimage);
            recents3 = view.findViewById(R.id.trimage);

            ArticleAdapter.glideIt(recents1, Fetching.myData.get(1).getP_photo(), getContext());
            ArticleAdapter.glideIt(recents2, Fetching.myData.get(2).getP_photo(), getContext());
            ArticleAdapter.glideIt(recents3, Fetching.myData.get(3).getP_photo(), getContext());

            pop1 = view.findViewById(R.id.fpimage); //fp = first popular
            pop2 = view.findViewById(R.id.spimage);
            pop3 = view.findViewById(R.id.tpimage);

            ArticleAdapter.glideIt(pop1, mySortedData.get(1).getP_photo(), getContext());
            ArticleAdapter.glideIt(pop2, mySortedData.get(2).getP_photo(), getContext());
            ArticleAdapter.glideIt(pop3, mySortedData.get(3).getP_photo(), getContext());

            shop1 = view.findViewById(R.id.fsimage); //fs = first shop
            shop2 = view.findViewById(R.id.ssimage);
            shop3 = view.findViewById(R.id.tsimage);

            shop1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    startActivity(myIntent);
                }
            });
            shop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    startActivity(myIntent);
                }
            });
            shop3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    startActivity(myIntent);
                }
            });
            sRecents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.putExtra("key", "2");
                    startActivity(myIntent);
                }
            });
            sPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.putExtra("key", "3");
                    startActivity(myIntent);
                }
            });
            sShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.putExtra("key", "1");
                    startActivity(myIntent);
                }
            });

        }
//Search engine
        searchResults = getView().findViewById(R.id.search_results);
        clear = getView().findViewById(R.id.clear);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        searchView = getView().findViewById(R.id.search);

        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        nameList = new ArrayList<>();
        photoList = new ArrayList<>();

        clear.setVisibility(View.INVISIBLE);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {


                if(!s.toString().isEmpty()){
                    clear.setVisibility(View.VISIBLE);

                    //calling the search engine activity and the function that handles the search
                    search.setAdapter(s.toString(),searchResults,getContext(),nameList,photoList);
                }
                else {
                    nameList.clear();
                    photoList.clear();
                    searchResults.removeAllViews();

                }

            }


        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
                hideKeyboard(getView());
            }
        });

        profile = getView().findViewById(R.id.profile_image);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ProfilePage.class);
                startActivity(intent);
            }
        });



        /*
        featured = Objects.requireNonNull(getView()).findViewById(R.id.featured);
        recents1 = getView().findViewById(R.id.frimage); //fr = first recent
        recents2 = getView().findViewById(R.id.srimage);
        recents3 = getView().findViewById(R.id.trimage);
        pop1 = getView().findViewById(R.id.fpimage); //fp = first popular
        pop2 = getView().findViewById(R.id.spimage);
        pop3 = getView().findViewById(R.id.tpimage);
        shop1 = getView().findViewById(R.id.fsimage); //fs = first shop
        shop2 = getView().findViewById(R.id.ssimage);
        shop3 = getView().findViewById(R.id.tsimage);

        ArrayList<individual_info_class> articles = new ArrayList<>(4);
        ArrayList<individual_info_class> pop_articles = new ArrayList<>(3);
        articles = Sorting.getItems(4);
        Sorting.get_Popular_Items(3, pop_articles);

        //Getting recents and populaire articles

        featured_info = articles.get(0);
        f_recent = articles.get(1);
        s_recent = articles.get(2);
        t_recent = articles.get(3);

        f_pop = pop_articles.get(0);
        s_pop = pop_articles.get(1);
        t_pop = pop_articles.get(2);

        //Displaying images
        */
        /*
        Glide.with(mContext)
                .load(featured_info.getP_photo())
                .into(featured);

        Glide.with(mContext)
                .load(f_recent.getP_photo())
                .into(recents1);
        Glide.with(mContext)
                .load(s_recent.getP_photo())
                .into(recents2);
        Glide.with(mContext)
                .load(t_recent.getP_photo())
                .into(recents3);

        Glide.with(mContext)
                .load(f_pop.getP_photo())
                .into(recents1);
        Glide.with(mContext)
                .load(s_pop.getP_photo())
                .into(recents2);
        Glide.with(mContext)
                .load(t_pop.getP_photo())
                .into(recents3);

        sRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Go to Bag Fragment
            }
        });
        sPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

         */
    }

    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

        ProgressDialog progressDialog;

        @Override
        protected Long doInBackground(URL... urls) {
            Fetching.getItems();
            FetchShops.getShops();
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

        }
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(),
                    "ProgressDialog",
                    "Wait for the items to load");
        }

    }
}

