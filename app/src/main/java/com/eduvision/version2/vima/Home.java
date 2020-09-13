package com.eduvision.version2.vima;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    Context mContext;

    TextView sRecents, sPop, sShop;

    ImageView featured, recents1, recents2, recents3, pop1, pop2, pop3, shop1, shop2, shop3;
    individual_info_class featured_info, f_recent, s_recent, t_recent, f_pop, s_pop, t_pop;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featured = Objects.requireNonNull(getView()).findViewById(R.id.featured);
        recents1 = getView().findViewById(R.id.frimage); //fr = first recent
        recents2 = getView().findViewById(R.id.srimage);
        recents3 = getView().findViewById(R.id.srimage);
        pop1 = getView().findViewById(R.id.fpimage); //fp = first popular
        pop2 = getView().findViewById(R.id.spimage);
        pop3 = getView().findViewById(R.id.tpimage);
        shop1 = getView().findViewById(R.id.fsimage); //fs = first shop
        shop2 = getView().findViewById(R.id.ssimage);
        shop3 = getView().findViewById(R.id.tsimage);

        sRecents = getView().findViewById(R.id.see_recents);
        sPop = getView().findViewById(R.id.see_popular);
        sShop = getView().findViewById(R.id.see_shops);

        ArrayList<individual_info_class> articles = new ArrayList<>(4);
        ArrayList<individual_info_class> pop_articles = new ArrayList<>(3);
        Sorting.getItems(4, articles);
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
    }
}
