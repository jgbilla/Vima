package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class shopPage extends AppCompatActivity {

    Article_info info;
    Article_description bDescription;
    Article_pictures bPictures;

    IndividualArticleConstructor individualArticleConstructor;
    private long article_id;
    Context mContext;
    FirebaseStorage myFireBaseStorage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    TextView price, title, description, shop_name, shop_description, shop_location;
    ImageView  big_pic, sm_pic1, sm_pic2, sm_pic3, sm_pic4, shop_pic;
    CircleImageView profile_picture;
    Spinner spin1, spin2;

    public void whatclick(View v){
        String number = "+22676603608";
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void callclick(View v){

        String number = "+22676603608";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public void smsclick(View v){
        String number = "+22676603608";
        String message = "J'ai connu votre boutique a travers l'applocation Vima";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+number));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indiv_shop_page);

        Bundle i = getIntent().getExtras();
        IndividualShop shop = new IndividualShop();
        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        if(i != null){
            shop = FetchShops.shopData.get(i.getInt("LockerKey"));
        }
        shop = FetchShops.shopData.get(1);
        ArrayList<Long> Articles1 = new ArrayList<>(1);
        ArrayList<Long> Articles2 = new ArrayList<>(1);
        ArrayList<Long> Articles3 = new ArrayList<>(1);

        Articles1 = (ArrayList<Long>) shop.shopMap.get(0);
        Articles2 = (ArrayList<Long>) shop.shopMap.get(1);
        Articles3 = (ArrayList<Long>) shop.shopMap.get(2);

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