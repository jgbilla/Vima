package com.eduvision.version2.vima;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.SearchEngine.Search_engine;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Verify;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Home extends Fragment {

    private static final int REQUEST_LOCATION = 1;
    TextView sRecents, sPop, sShop;
    ProgressBar Progress;
    ArrayList<String> nameList;
    ArrayList<StorageReference> photoList;
    Search_engine search = new Search_engine();
    Button clear;
    ImageView featured, recents1, recents2, recents3, pop1, pop2, pop3, shop1, shop2, shop3;
    EditText searchView;
    RecyclerView searchResults;
    DatabaseReference databaseReference;
    ImageView profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    public Home() {
        // Required empty public constructor
    }


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
    // TODO: Rename and change types and number of parameters
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
    public ArrayList<IndividualArticle> sort(@NonNull ArrayList<IndividualArticle> myList1){
        for(int i = 0; i<(myList1.size()-1); i++) {
                for (int a = 0; a < (myList1.size()-1); a++) {
                    if (myList1.get(a).getPopularity_index() > myList1.get(a + 1).getPopularity_index()) {
                        IndividualArticle temp = myList1.get(a + 1);
                        myList1.set(a + 1, myList1.get(a));
                        myList1.set(a, temp);
                    }
                }
            }
            return myList1;
    }
    private AdView mAdView;

    private void glideAll(){
        ArticleAdapter.glideIt(shop1, Spinning.shopData.get(1).getP_photo(), getContext());
        ArticleAdapter.glideIt(shop2, Spinning.shopData.get(2).getP_photo(), getContext());
        ArticleAdapter.glideIt(shop3, Spinning.shopData.get(3).getP_photo(), getContext());

        ArticleAdapter.glideIt(recents1, Spinning.myData.get(1).getP_photo(), getContext());
        ArticleAdapter.glideIt(recents2, Spinning.myData.get(2).getP_photo(), getContext());
        ArticleAdapter.glideIt(recents3, Spinning.myData.get(3).getP_photo(), getContext());

        ArticleAdapter.glideIt(pop1, mySortedData.get(1).getP_photo(), getContext());
        ArticleAdapter.glideIt(pop2, mySortedData.get(2).getP_photo(), getContext());
        ArticleAdapter.glideIt(pop3, mySortedData.get(3).getP_photo(), getContext());
    }
    public static ArrayList<IndividualArticle> mySortedData = new ArrayList<>(80);
    static boolean firstTime = true;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(firstTime){
            Fetching.makeCustomToast(getContext(), "Bienvenue sur VIMA", Toast.LENGTH_LONG);
        }
        firstTime = false;

        /*
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

         */

        featured = view.findViewById(R.id.featured);
        Progress = Objects.requireNonNull(getView()).findViewById(R.id.linlaHeaderProgress);
        mySortedData = sort(Spinning.myData);
        Progress.setVisibility(View.GONE);

        sRecents = view.findViewById(R.id.see_recents);
        sPop = view.findViewById(R.id.see_popular);
        sShop = view.findViewById(R.id.see_shops);

        sRecents = view.findViewById(R.id.see_recents);
        sPop = view.findViewById(R.id.see_popular);
        sShop = view.findViewById(R.id.see_shops);

        shop1 = view.findViewById(R.id.fsimage);
        shop2 = view.findViewById(R.id.ssimage);
        shop3 = view.findViewById(R.id.tsimage);

        recents1 = view.findViewById(R.id.frimage); //fr = first recent
        recents2 = view.findViewById(R.id.srimage);
        recents3 = view.findViewById(R.id.trimage);

        pop1 = view.findViewById(R.id.fpimage); //fp = first popular
        pop2 = view.findViewById(R.id.spimage);
        pop3 = view.findViewById(R.id.tpimage);

        featured = view.findViewById(R.id.featured);
        ImageButton goLeft = view.findViewById(R.id.goLeft);
        ImageButton goRight = view.findViewById(R.id.goRight);
        TextView shopName = view.findViewById(R.id.shopName);

        if(Fetching.waitInternetAvailable(getContext())) {
            int[] featuredCounter = {0};
            ArticleAdapter.glideIt(featured, Spinning.myData.get(0).getP_photo(), getContext());
            shopName.setText(Spinning.myData.get(0).getShop_name());
            glideAll();
            goLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (featuredCounter[0] >= 1 && featuredCounter[0] < Fetching.myData.size()) {
                        featuredCounter[0]--;
                        ArticleAdapter.glideIt(featured,
                                Spinning.myData.get(featuredCounter[0]).getP_photo(),
                                getContext());
                        shopName.setText(Spinning.myData.get(featuredCounter[0]).getShop_name());
                    }
                }
            });

            goRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (featuredCounter[0] + 1 < Fetching.myData.size()) {
                        featuredCounter[0]++;
                        ArticleAdapter.glideIt(featured,
                                Spinning.myData.get(featuredCounter[0]).getP_photo(),
                                getContext());
                        shopName.setText(Spinning.myData.get(featuredCounter[0]).getShop_name());
                    }
                }
            });

            pop1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.putExtra("LockerKey", mySortedData.get(0).positionInDataBase);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });

            pop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.putExtra("LockerKey", mySortedData.get(1).positionInDataBase);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });

            pop3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.putExtra("LockerKey", mySortedData.get(2).positionInDataBase);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });

            recents1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    myIntent.putExtra("LockerKey", 0);
                    startActivity(myIntent);
                }
            });

            recents2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.putExtra("LockerKey", 1);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });

            recents3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), articlePage.class);
                    myIntent.putExtra("LockerKey", 2);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });

            shop1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    myIntent.putExtra("LockerKey", 0);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });
            shop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    myIntent.putExtra("LockerKey", 1);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });
            shop3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), shopPage.class);
                    myIntent.putExtra("LockerKey", 2);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });
            sRecents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.putExtra("key", "2");
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });
            sPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.putExtra("key", "3");
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);
                }
            });
            sShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), Verify.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    myIntent.putExtra("key", "1");
                    startActivity(myIntent);
                }
            });
        }
        else{
            Fetching.makeCustomToast(getContext(), "Connectez-vous à Internet", Toast.LENGTH_LONG);
        }
        SwipeRefreshLayout myRefreshLayout = view.findViewById(R.id.pullToRefresh);
        myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Spinning.isDataFetched = false;
                new DownloadFilesTask().execute();

                if (!Fetching.isInternetAvailable(getApplicationContext())) {
                    //...
                    myRefreshLayout.setRefreshing(false);
                    Fetching.makeCustomToast(getApplicationContext(), "Pas de Connexion Internet", Toast.LENGTH_LONG);

                } else {
                    if (!Fetching.isDataFetched) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!Fetching.isDataFetched) {
                                    //...
                                    myRefreshLayout.setRefreshing(false);
                                    Fetching.makeCustomToast(getApplicationContext(), "Réessayez", Toast.LENGTH_LONG);
                                } else {
                                    glideAll();
                                    myRefreshLayout.setRefreshing(false);
                                }
                            }
                        }, 1000);
                    } else {
                        glideAll();
                        myRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });

        //Setting up the search engine
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
                    clear.
                            setVisibility(View.VISIBLE);

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

        //Access Profile


        profile = getView().findViewById(R.id.profile_image);
        String profilePicture = sharedPreferences.getString("profile", "https://www.google.com/search?q=placeholder+profile+pictures+free+to+use&tbm=isch&ved=2ahUKEwjA6ZvV2tDrAhUElBoKHd_bDRIQ2-cCegQIABAA&oq=placeholder+profile+pictures+free+to+use&gs_lcp=CgNpbWcQAzoECAAQHlC7YVixcGCvcWgAcAB4AIAB5QWIAbsVkgEHNC0zLjEuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=ANVSX8DpHoSoat-3t5AB&bih=792&biw=1536#imgrc=_JeJ3jskVgcZaM");
        Glide.with(getApplicationContext())
                .load(profilePicture)
                .placeholder(R.drawable.categorie_enfant)
                .into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ProfilePage.class);
                startActivity(intent);
            }
        });
    }
    private void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}
