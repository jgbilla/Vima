package com.eduvision.version2.vima;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    ViewPager viewPager;
    MyPagerAdapter adapterViewPager;
    TextView sRecents, sPop, sShop;
    View view;
    ImageView recents1, recents2, recents3, pop1, pop2, pop3, shop1, shop2, shop3;
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
        if (Objects.requireNonNull(getArguments()).getInt(ARG_PARAM1) == 1){
            view = inflater.inflate(R.layout.fragment_home, container, false);
            viewPager = view.findViewById( R.id.my_pager);
            viewPager.setCurrentItem(getArguments().getInt(ARG_PARAM2));
            adapterViewPager = new MyPagerAdapter(((FragmentActivity) view.getContext()).getSupportFragmentManager());
            viewPager.setAdapter(adapterViewPager);
            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
        }
        else{
            //TODO: Improve acceuil_layout
            view = inflater.inflate(R.layout.acceuil_layout, container,false);
            recents1 = view.findViewById(R.id.first_recent_image);
            recents2 = view.findViewById(R.id.second_recent_image);
            recents3 = view.findViewById(R.id.third_recent_image);
            pop1 = view.findViewById(R.id.first_popular_image);
            pop2 = view.findViewById(R.id.second_popular_image);
            pop3 = view.findViewById(R.id.third_popular_image);
            shop1 = view.findViewById(R.id.first_shop_image);
            shop2 = view.findViewById(R.id.second_shop_image);
            shop3 = view.findViewById(R.id.third_shop_image);

            sRecents = view.findViewById(R.id.see_recents);
            sPop = view.findViewById(R.id.see_popular);
            sShop = view.findViewById(R.id.see_shops);

            Recents.getItems(100);
            ArrayList<individual_info_class> pop_list = Recents.getMyList();
            //Take the info you need for the recents images
            pop_list.sort(Comparator.comparingInt(individual_info_class::getPopularity_index).reversed());
            //Now take the info you need for the popular images
            individual_info_class first = new individual_info_class();

            sRecents.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Home.newInstance(1, 0);
                }
            });
            sPop.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Home.newInstance(1, 1);
                }
            });
            sShop.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Home.newInstance(1, 2);
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //********************************************************************************************************************************************
    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = 3;
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "Recents";
                case 1:
                    return "Populaire";
                case 2:
                    return "Boutiques";
                default:
                    return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Recents.newInstance(0, "Page # 1");
                case 1:
                    return Popular.newInstance(1, "Page # 2");
                case 2:
                    return Recents.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }
    }
}
