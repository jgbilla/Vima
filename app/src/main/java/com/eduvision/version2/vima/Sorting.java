package com.eduvision.version2.vima;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class Sorting {

    protected static void getItems(final int n, @NonNull ArrayList<individual_info_class> myList){
        final int[] lastId = new int[1];
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastId[0] = (int) dataSnapshot.getChildrenCount();
                String name;
                String price;
                String photo;
                final String[] shop = new String[1];
                int shop_id;
                for(int i=0; i<(n-1); i++){
                    final individual_info_class infos = new individual_info_class();
                    name = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("name").getValue().toString();
                    infos.setName(name);
                    price = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("price").getValue().toString();
                    infos.setPrice(price);
                    photo = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("pictures").child("p_photos").getValue().toString();
                    infos.setP_photo(photo);
                    shop_id = (int) dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("seller_id").getValue();
                    infos.setSeller_id(shop_id);
                    final int finalShop_id = shop_id;
                    mDatabase.child("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            shop[0] = dataSnapshot2.child(Integer.toString(finalShop_id)).child("infos").child("name").toString();
                            infos.setShop_name(shop[0]);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    infos.setRank(lastId[0] -i);
                    myList.add(infos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static void get_Popular_Items(final int n, @NonNull ArrayList<individual_info_class> myList){
        getItems(n, myList);
        myList.sort(Comparator.comparingInt(individual_info_class::getPopularity_index).reversed());
    }

}
