package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Spinning extends AppCompatActivity {
    View progress;
    Button tryAgain;
    static TextView progressText;
    static Context myContext;

    public void getLikedItems(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String LikedItems = prefs.getString("LikedItems", null);
        Type type = new TypeToken<ArrayList<IndividualArticle>>() {}.getType();
        if (LikedItems != null && !LikedItems.equals("")) {
            Recents.myLikedItems = gson.fromJson(LikedItems, type);
        }
    }

    public void updateNumbUsersConnectedToday() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String lastDate = prefs.getString("date", date);
        int numbConnectedToday = prefs.getInt("numbConnectedToday", 1);

        if (lastDate.equals(date)) {
            numbConnectedToday++;
            editor.putBoolean("connectedToday", true);
            editor.putInt("numbConnectedToday", numbConnectedToday);
            editor.apply();
        }
        else {
            mDatabase.child("Stats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    editor.putBoolean("connectedToday", true);
                    editor.putInt("numbConnectedToday", 1);
                    editor.apply();
                    String counter = String.valueOf(snapshot.child("User Connections on " + lastDate).getValue());
                    mDatabase.child("Stats").child("User Connections on " + lastDate).setValue(
                            Integer.parseInt(counter) + 1
                    );

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNumbUsersConnectedToday();

        getLikedItems();
        setContentView(R.layout.spinning_activity);
        myContext = getApplicationContext();
        progress = findViewById(R.id.progress);
        tryAgain = findViewById(R.id.retry);
        progressText = findViewById(R.id.progressText);
        getArticles();
    }


    public void setProgressInt(int progress) {
        progressText.setText("Progrès: " + progress + "%");
    }



















    public static ArrayList<IndividualShop>
            shopData = new ArrayList<>(80);
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

        int counterA = 1;
        if(snapshot.getChildrenCount() < 80){
            counterA = 80;
        }
        else{
            counterA = (int) snapshot.getChildrenCount();
        }

        for(int i = 1; i<=(counterA); i++){
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
                for (int a= 1; a<30; a++){
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
        int counterS = 1;
            counterS = counterN;

        for(int i = 1; i<(counterS); i++){
                currentArticleN = snapshot.child(Integer.toString(i)).getValue(IndividualArticle.class);

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