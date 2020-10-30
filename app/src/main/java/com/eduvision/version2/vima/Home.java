package com.eduvision.version2.vima;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Verify;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Home extends Fragment {
    private static final int REQUEST_LOCATION = 1;
    TextView sRecents, sPop, sShop;
    ProgressBar Progress;

    ImageView featured, recents1, recents2, recents3, pop1, pop2, pop3, shop1, shop2, shop3;

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

    public static ArrayList<IndividualArticle> mySortedData = new ArrayList<>(80);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        featured = view.findViewById(R.id.featured);
        Progress = Objects.requireNonNull(getView()).findViewById(R.id.linlaHeaderProgress);
        mySortedData = sort(Fetching.myData);
        Progress.setVisibility(View.GONE);

        sRecents = view.findViewById(R.id.see_recents);
        sPop = view.findViewById(R.id.see_popular);
        sShop = view.findViewById(R.id.see_shops);

        ImageButton goLeft = view.findViewById(R.id.goLeft);
        ImageButton goRight = view.findViewById(R.id.goRight);
        TextView shopName = view.findViewById(R.id.shopName);

        int[] featuredCounter = {0};
        ArticleAdapter.glideIt(featured, Fetching.myData.get(0).getP_photo(), getContext());
        shopName.setText(Fetching.myData.get(0).getShop_name());
        goLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredCounter[0] - 1 > 0 && featuredCounter[0] - 1 < Fetching.myData.size()) {
                    featuredCounter[0]--;
                    ArticleAdapter.glideIt(featured,
                            Fetching.myData.get(featuredCounter[0] + 3).getP_photo(),
                            getContext());
                    shopName.setText(Fetching.myData.get(featuredCounter[0] + 3).getShop_name());
                }
            }
        });

        goRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredCounter[0] + 1 < Fetching.myData.size()) {
                    featuredCounter[0]++;
                    ArticleAdapter.glideIt(featured,
                            Fetching.myData.get(featuredCounter[0] + 3).getP_photo(),
                            getContext());
                    shopName.setText(Fetching.myData.get(featuredCounter[0] + 3).getShop_name());
                }
            }
        });

        sRecents = view.findViewById(R.id.see_recents);
        sPop = view.findViewById(R.id.see_popular);
        sShop = view.findViewById(R.id.see_shops);

        shop1 = view.findViewById(R.id.fsimage);
        shop2 = view.findViewById(R.id.ssimage);
        shop3 = view.findViewById(R.id.tsimage);

        featured = view.findViewById(R.id.featured);
        ArticleAdapter.glideIt(shop1, FetchShops.shopData.get(1).getP_photo(), getContext());
        ArticleAdapter.glideIt(shop2, FetchShops.shopData.get(2).getP_photo(), getContext());
        ArticleAdapter.glideIt(shop3, FetchShops.shopData.get(3).getP_photo(), getContext());

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

        shop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), shopPage.class);
                myIntent.putExtra("LockerKey", 0);
                startActivity(myIntent);
            }
        });
        shop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), shopPage.class);
                myIntent.putExtra("LockerKey", 1);
                startActivity(myIntent);
            }
        });
        shop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), shopPage.class);
                myIntent.putExtra("LockerKey", 0);
                myIntent.putExtra("LockerKey", 2);
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
}
