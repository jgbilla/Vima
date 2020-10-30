package com.eduvision.version2.vima.Tabs;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FetchShops {
    public static ArrayList<IndividualShop> shopData = new ArrayList<>(80);
    public static String isShopsDataFetched = "No";
    public static String isShopsDataBeingFetched = "No";
    public static ArrayList<IndividualShop> homeShopsData = new ArrayList<>(4);

    public static void getShops(){
        isShopsDataBeingFetched = "Yes";
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                        for (int a= 1; a<160; a++){
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
                isShopsDataFetched = "Yes";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
