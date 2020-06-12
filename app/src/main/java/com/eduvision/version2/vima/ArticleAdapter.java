package com.eduvision.version2.vima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
/*
I used this class as a BaseAdapter for the "recent" Activity
 */

public class ArticleAdapter extends BaseAdapter {

    String nameA, priceA, typeA, photoA, shopA;
    Context mContext; boolean isLiked = false;
    private DatabaseReference mDatabase;
    private FirebaseStorage myFireBaseStorage;

    public ArticleAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override

    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //******************************************************************************************************************
    //Creating the View that will be passed on
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArrayList<ArrayList<String>> myList;
        myList = OrangeMoney.getMyList();
        final ArrayList<String> temp = myList.get(position);
        nameA = temp.get(1);
        priceA = temp.get(2);
        photoA = temp.get(3);
        shopA = temp.get(4);
        myFireBaseStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.model, null);
        }
        final ImageButton like_button = convertView.findViewById(R.id.like_button);
        final TextView name = convertView.findViewById(R.id.nameA);
        final ImageView photo = convertView.findViewById(R.id.photo);
        final TextView price = convertView.findViewById(R.id.price);
        final TextView shop = convertView.findViewById(R.id.shop);

        StorageReference storageReference = myFireBaseStorage.getReferenceFromUrl(photoA);
        Glide.with(mContext)
                .load(storageReference)
                .into(photo);
        name.setText(nameA);
        price.setText(priceA);
        shop.setText(shopA);

        //Handling the like option
        like_button.setImageResource(R.drawable.likeA);

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked;
                if(isLiked == true){
                    final int[] counter = new int[1];
                    like_button.setImageResource(R.drawable.likeB);
                    mDatabase.child("articles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String id = temp.get(5);
                            counter[0] = (int) dataSnapshot.child(id).child("infos").child("likes").getValue();
                            mDatabase.child("articles").child("info").child("likes").setValue(counter[0]++);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    like_button.setImageResource(R.drawable.likeA);
                    final int[] counter = new int[1];
                    mDatabase.child("articles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String id = temp.get(5);
                            counter[0] = (int) dataSnapshot.child(id).child("infos").child("likes").getValue();
                            mDatabase.child("articles").child("info").child("likes").setValue(counter[0]--);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        return convertView;
    }
}