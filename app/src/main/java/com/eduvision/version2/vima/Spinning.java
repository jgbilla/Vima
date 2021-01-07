package com.eduvision.version2.vima;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.eduvision.version2.vima.Tabs.Boutiques;
import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Popular;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static com.eduvision.version2.vima.Tabs.Fetching.isDataFetched;


public class Spinning extends AppCompatActivity {
    View progress;
    Button tryAgain;
    static TextView progressText;
    static Context myContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinning_activity);
        myContext = getApplicationContext();
        progress = findViewById(R.id.progress);
        tryAgain = findViewById(R.id.retry);
        progressText = findViewById(R.id.progressText);
        getArticles();
    }


    public void setProgressInt(int progress) {
        progressText.setText("Progrès: " + progress);
    }



















    public static ArrayList<IndividualShop> shopData = new ArrayList<>(80);
    public static boolean isShopsDataFetched = false;
    public static ArrayList<IndividualShop> homeShopsData = new ArrayList<>(4);

    public static ArrayList<IndividualArticle> myData = new ArrayList<>(80);
    public static boolean isDataFetched = false;
    public static boolean isHomeDataFetched = false;
    public static ArrayList<IndividualArticle> homeArticlesData = new ArrayList<>(4);

    public void cleaningUpDate(DataSnapshot shops, DataSnapshot items){
        DataSnapshot snapshot;
        snapshot = shops;
        shopData.clear();
        IndividualShop currentArticle;
        String sCounter = snapshot.child("counter").getValue().toString();
        int counter = Integer.parseInt(sCounter);
        DataSnapshot mSnapshot;

        for(int i = 1; i<=(16); i++){
            if(i < counter){
                currentArticle = snapshot.child(String.valueOf(i)).getValue(IndividualShop.class);
                mSnapshot = snapshot.child(Integer.toString(i)).child("Articles");
            }
            else{
                currentArticle = snapshot.child(sCounter).getValue(IndividualShop.class);
                mSnapshot = snapshot.child(sCounter).child("Articles");
            }

            ArrayList<String> Titles = new ArrayList<>(1);
            for(int c= 1; c<=3; c++) {
                ArrayList<Long> ArticlesArray = new ArrayList<>(1);
                for (int a= 1; a<80; a++){
                    DataSnapshot cSnapshot;
                    if(a<mSnapshot.child(String.valueOf(c)).getChildrenCount()){
                        cSnapshot = mSnapshot.child(String.valueOf(c)).child(String.valueOf(a));
                    }
                    else{
                        cSnapshot = mSnapshot.child(String.valueOf(c)).child("1");
                    }
                    ArticlesArray.add(Long.parseLong(String.valueOf(cSnapshot.getValue())));
                }
                Titles.add(String.valueOf(mSnapshot.child(String.valueOf(c)).child("name").getValue()));
                currentArticle.shopMap.add(ArticlesArray);
            }
            currentArticle.setTitles(Titles);

            Objects.requireNonNull(currentArticle).positionInDataBase = i;
            if(i < 3){
                homeShopsData.add(currentArticle);
            }
            shopData.add(currentArticle);
        }
        isShopsDataFetched = true;



        snapshot = items;

        myData.clear();
        IndividualArticle currentArticleN;
        String sCounterN = snapshot.child("counter").getValue().toString();
        int counterN = Integer.parseInt(sCounterN);
        for(int i = 1; i<=(80); i++){
            if(i < counterN){
                currentArticleN = snapshot.child(Integer.toString(i)).getValue(IndividualArticle.class);
            }
            else{
                currentArticleN = snapshot.child(sCounter).getValue(IndividualArticle.class);
            }
            Objects.requireNonNull(currentArticleN).positionInDataBase = i;

            if(i < 5){
                if (i == 5){
                    isHomeDataFetched = true;
                    setProgressInt(90);
                }
                homeArticlesData.add(currentArticleN);
            }
            myData.add(currentArticleN);
        }
        isDataFetched = true;

    }
    public void getArticles(){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setProgressInt(25);
                    mDatabase.child("Articles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Nsnapshot) {
                            setProgressInt(75);
                            cleaningUpDate(snapshot, Nsnapshot);
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }























}