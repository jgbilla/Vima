package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eduvision.version2.vima.R;
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
                for(int i = 1; i<=(16); i++){
                    if(i < counter){
                        currentArticle = snapshot.child(Integer.toString(i)).getValue(IndividualShop.class);
                    }
                    else{
                        currentArticle = snapshot.child(sCounter).getValue(IndividualShop.class);
                    }
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
