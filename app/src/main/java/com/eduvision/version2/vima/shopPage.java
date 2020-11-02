package com.eduvision.version2.vima;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class shopPage extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indiv_shop_page);

        ImageButton goBack = findViewById(R.id.go_back);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
        IndividualShop shop = new IndividualShop();

        Bundle i = getIntent().getExtras();
        if(i != null){
            shop = FetchShops.shopData.get(i.getInt("LockerKey"));
        }
        TextView description = findViewById(R.id.shop_page_description);
        description.setText(shop.getName());

        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        ArrayList<Long> Articles1;
        ArrayList<Long> Articles2;
        ArrayList<Long> Articles3;

        Articles1 = shop.shopMap.get(0);
        Articles2 = shop.shopMap.get(1);
        Articles3 = shop.shopMap.get(2);

        adapter.addFragment(new ImagesTabs(Articles1), shop.myTitles.get(0));
        adapter.addFragment(new ImagesTabs(Articles2), shop.myTitles.get(1));
        adapter.addFragment(new ImagesTabs(Articles3), shop.myTitles.get(2));

        viewPager = findViewById(R.id.shop_view_pager);
        tabLayout = findViewById(R.id.shopTabLayout);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                position = 3;

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                position = 0;

            }
        });
    }
}

class CustomAdapter extends FragmentPagerAdapter {
    public CustomAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @Override
    public int getCount() {
            return mFragmentList.size();
            }

    @NonNull
    @Override
    public Fragment getItem(int position) {
            return mFragmentList.get(position);
            }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        this.notifyDataSetChanged();
        }
    @Override
    public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
            }
}