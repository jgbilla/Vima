package com.eduvision.version2.vima.SearchEngine;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.ShopModel;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Search_engine extends AppCompatActivity {


    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_engine_layout);

    }



    public void setResearch(ArrayList<IndividualArticle> data, String searchedString, RecyclerView searchResults, Context context, ArrayList<IndividualArticle> articleList, ArrayList<ShopModel> shopList, ArrayList<String> type, ArrayList<Integer> position) {
        int[] counter = new int[] {0,0};



       /* databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                nameList.clear();
                photoList.clear();
                searchResults.removeAllViews();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String id= snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String photo = snapshot.child("p_photo").getValue(String.class);
                    String type = "article";

                    counter[0]++;

                    if (photo!= null && name.toLowerCase().contains(searchedString.toLowerCase())) {

                        nameList.add(name);

                        String [] finalphoto = photo.split("/");
                        String test = finalphoto[finalphoto.length-1];
                        StorageReference reference = FirebaseStorage.getInstance().getReference().child(finalphoto[finalphoto.length-2]).child(test);

                        //ToDo: All images should have the same name as the item so that we can retrieve them easily. Add this feature here.

                        photoList.add(reference);
                        typeList.add(type);
                        Log.d(TAG, "Value is: " + photoList);


                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String name = snapshot1.child("name").getValue(String.class);
                    String photo = snapshot1.child("p_photo").getValue(String.class);
                    String type ="shop";

                    counter[0]++;

                    if (photo!= null && name.toLowerCase().contains(searchedString.toLowerCase())) {

                        nameList.add(name);

                        String [] finalphoto = photo.split("/");
                        String test = finalphoto[finalphoto.length-1];
                        StorageReference reference = FirebaseStorage.getInstance().getReference().child(finalphoto[finalphoto.length-2]).child(test);

                        //ToDo: All images should have the same name as the item so that we can retrieve them easily. Add this feature here.

                        photoList.add(reference);
                        typeList.add(type);
                        Log.d(TAG, "Value is: " + photoList);


                    }
                    if (counter[0] ==15){
                        break;
                    }
                }*/



        articleList.clear();
        shopList.clear();
        type.clear();
        searchResults.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            IndividualArticle article = data.get(i);
            String name = article.getName();
            String photo = article.getP_photo();



            if (photo != null && name.toLowerCase().contains(searchedString.toLowerCase())) {
                type.add("Article");
                articleList.add(article);
                counter[0]++;


            }

            if (counter[0] == 3){
                break;
            }



        }

        for (int i = 0; i < Spinning.shopData.size(); i++) {
            ShopModel shop = Spinning.shopData.get(i);
            String name = shop.getName();
            String photo = shop.getP_photo();



            if (photo != null && name.toLowerCase().contains(searchedString.toLowerCase())) {
                type.add("Boutique");
                shopList.add(shop);
                counter[1]++;
            }

            if (counter[1] == 2){
                break;
            }
        }
        if(articleList.isEmpty() && shopList.isEmpty()){
            type.add("Article");
            IndividualArticle empty = new IndividualArticle();
            empty.setName("Aucun article ne repond a votre recherche");
            empty.setP_photo("gs://delivery-1b761.appspot.com/Coding tools/folder.png");
            articleList.add(empty);
        }





        SearchAdapter searchAdapter = new SearchAdapter(context, articleList, shopList,type,position);
        searchResults.setAdapter(searchAdapter);
    }
    }






