package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.ImagesTabs;
import com.eduvision.version2.vima.ProfilePage;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Verify extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentManager mf;
    TabAdapter adapter;
    public void putLikedItems(){
        if(Recents.myLikedItems != null){
            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String jsonText = gson.toJson(Recents.myLikedItems);
            editor.putString("LikedItems", jsonText);
            editor.apply();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    int resultCodeForPopularFilter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        int myCase = 0;
        resultCodeForPopularFilter = 2;
        if (extras != null){
            myCase = Integer.parseInt(extras.getString("key"));
            if(extras.getString("resultCode")!=null) {
                resultCodeForPopularFilter = Integer.parseInt(extras.getString("resultCode"));
                Log.println(Log.DEBUG, "Tag", String.valueOf(resultCodeForPopularFilter));
            }
        }
        FragmentManager fm = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.orange);
        viewPager = findViewById(R.id.verify_view_pager);
        tabLayout = findViewById(R.id.verify_tabLayout);
        adapter = new TabAdapter(fm);
        Popular myPopular = new Popular();
        myPopular.resultCodeForPopularFilter = this.resultCodeForPopularFilter;
        adapter.addFragment(new Boutiques(), "Boutiques");
        adapter.addFragment(new Recents(), "Recents");
        adapter.addFragment(myPopular, "Populaires");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(myCase-1);
        tabLayout.setupWithViewPager(viewPager);

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
                Intent intent= new Intent(Verify.this, ProfilePage.class);
                startActivity(intent);
            }
        });

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



    /***************************************************************************************************************/
    public boolean isConnected(Context c){
        /*
        This method will check if the phone possesses an internet connection.
         */
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

    /***************************************************************************************************************/
    private int checkPassword(String password){
        /* This method will check if the password is strong enough.
         * Returns 1 if everything is fine
         * Returns 2 if the length is less than 8
         * Returns 3 if there is a whitespace
         * Returns 4 if there are fewer than 3 digits or 3 letters or 3 others as required*/
        int length = password.length();
        int result;
        boolean white = false;
        int b, c, d;
        b = c = d = 0;
        if (length>8) {
            for (int i = 0; i < length; i++) {
                if (Character.isWhitespace(password.charAt(i))) {
                    white = true;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Character.isAlphabetic(password.charAt(i))) {
                        c++;
                    } else if (Character.isDigit(password.charAt(i))) {
                        b++;
                    } else {
                        d++;
                    }
                }
            }
        }
        if((b > 3) && (c > 3) && (d > 3)) {
            if (!white) {
                result = 1;
            } else {
                result = 3;
            }
        }
        else{
            result = 4;
        }
        return result;

    }

    /***************************************************************************************************************/
    private boolean isValidEmail(CharSequence target){
        /*
        This method will check if the given email follows the general pattern of email addresses.
         */
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}