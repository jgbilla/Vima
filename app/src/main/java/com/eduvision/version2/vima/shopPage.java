package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class shopPage extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    public void whatclick(IndividualShop article){
        String number = article.getPhone();
        String message = createMessage(article);
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+number + "&text=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void callclick(IndividualShop article){

        String number = article.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public String createMessage(IndividualShop article){
        String result = "J'ai connu votre boutique a travers l'application Vima. J'aimerais recevoir plus d'information sur les articles que vous proposez s'il vous plait.";
        return result;
    }
    public void smsclick(IndividualShop article){
        String number = article.getPhone();
        String message = createMessage(article);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+number));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_indiv_shop_page);

        ImageButton goBack = findViewById(R.id.go_back);

        IndividualShop shop = new IndividualShop();
        Bundle i = getIntent().getExtras();
        if(i != null){
            shop = Spinning.shopData.get(i.getInt("LockerKey"));
        }
        final IndividualShop finalShop = shop;

        ImageButton call, sms, what;
        call = findViewById(R.id.callclick);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callclick(finalShop);
            }
        });
        sms = findViewById(R.id.smsclick);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsclick(finalShop);
            }
        });
        what = findViewById(R.id.whatclick);
        what.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatclick(finalShop);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });

        ImageView bigImage = findViewById(R.id.big_picture);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(shop.getP_photo());

        Glide.with(getApplicationContext())
                .load(storageReference)
                .into(bigImage);

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

        ImageView profile = findViewById(R.id.profile_image);
        SharedPreferences sharedPreferences = sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        String profilePicture = sharedPreferences.getString("profile", "https://www.google.com/search?q=placeholder+profile+pictures+free+to+use&tbm=isch&ved=2ahUKEwjA6ZvV2tDrAhUElBoKHd_bDRIQ2-cCegQIABAA&oq=placeholder+profile+pictures+free+to+use&gs_lcp=CgNpbWcQAzoECAAQHlC7YVixcGCvcWgAcAB4AIAB5QWIAbsVkgEHNC0zLjEuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=ANVSX8DpHoSoat-3t5AB&bih=792&biw=1536#imgrc=_JeJ3jskVgcZaM");
        Glide.with(getApplicationContext())
                .load(profilePicture)
                .placeholder(R.drawable.categorie_enfant)
                .into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
        });

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