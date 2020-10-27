package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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
    TabAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_indiv_shop_page);

        viewPager = findViewById(R.id.shop_view_pager);
        tabLayout = findViewById(R.id.shopTabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImagesTabs(), "Tout");
        adapter.addFragment(new ImagesTabs(), "Robes");
        adapter.addFragment(new ImagesTabs(), "Blazers");
        adapter.addFragment(new ImagesTabs(), "Vestes");
        adapter.addFragment(new ImagesTabs(), "A propos");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
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
