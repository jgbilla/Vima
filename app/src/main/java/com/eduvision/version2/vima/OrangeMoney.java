package com.eduvision.version2.vima;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrangeMoney extends AppCompatActivity {
    int lastId;
    private boolean transfer = false;
    private DatabaseReference mDatabase;
    GridView articlegv;
    public static ArrayList<ArrayList<String>> myList;

    public static ArrayList<ArrayList<String>> getMyList() {
        return myList;
    }

    private void getItems(){
        mDatabase.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastId = (int) dataSnapshot.getChildrenCount();
                String name;
                String price;
                String photo;
                final String[] shop = new String[1];
                int shop_id;
                for(int i=0; i<30; i++){
                    ArrayList<String> info = new ArrayList<String>(5);
                    name = dataSnapshot.child(Integer.toString(lastId-i)).child("infos").child("name").getValue().toString();
                    info.add(name);
                    price = dataSnapshot.child(Integer.toString(lastId-i)).child("infos").child("price").getValue().toString();
                    info.add(price);
                    photo = dataSnapshot.child(Integer.toString(lastId-i)).child("pictures").child("p_photo").getValue().toString();
                    info.add(photo);
                    shop_id = (int) dataSnapshot.child(Integer.toString(lastId-i)).child("infos").child("seller_id").getValue();
                    info.add(photo);
                    final int finalShop_id = shop_id;
                    mDatabase.child("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            shop[0] = dataSnapshot2.child(Integer.toString(finalShop_id)).child("infos").child("name").toString();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    info.add(shop[0]);
                    info.add(Integer.toString(lastId-i));
                    myList.add(info);
                }
                transfer = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myList = new ArrayList<>(0);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orangemoney);
        articlegv = findViewById(R.id.gridview);
        getItems();
        while(!transfer){
            try {
                //Just waiting that the loading of the getItems function is done
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        articlegv.setAdapter(new ArticleAdapter(OrangeMoney.this));
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*Get to specific chosen Article page,It is not complete yet tho*/
                Intent myIntent = new Intent(OrangeMoney.this, articlePage.class);
                myIntent.putExtra("id", id);
                startActivity(myIntent);
            }
        });
    }
}
