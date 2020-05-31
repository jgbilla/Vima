package com.eduvision.version2.vima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/*
I used this class as a BaseAdapter for the "recent" Activity
 */



public class ArticleAdapter extends BaseAdapter {
    /*
    Global variables:
    lastId: id of the most recent article given by orangmoney activity
    nameA, priceA and typeA: the title, price and short description of the displayed article
     */
    final int lastId;
    String nameA, priceA, typeA, photoA;
    Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseStorage myFireBaseStorage;

    public ArticleAdapter(Context context, int id) {
        this.mContext = context;
        this.lastId = id;
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
        return lastId-position;
    }

    //******************************************************************************************************************
    //Creating the View that will be passed on
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get the article name
        mDatabase.child("Articles").child(Integer.toString(lastId-position)).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameA = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Get the article price
        mDatabase.child("Articles").child(Integer.toString(lastId-position)).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                priceA = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Get the article type
        mDatabase.child("Articles").child(Integer.toString(lastId-position)).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                typeA = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Get the article photo location
        mDatabase.child("Articles").child(Integer.toString(lastId-position)).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photoA = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.model, null);
        }

        myFireBaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = myFireBaseStorage.getReferenceFromUrl(photoA);
        final TextView name = convertView.findViewById(R.id.name);
        final ImageView photo = convertView.findViewById(R.id.photo);
        final TextView price = convertView.findViewById(R.id.price);
        final TextView type = convertView.findViewById(R.id.type);

        Glide.with(mContext)
                .load(storageReference)
                .into(photo);

        name.setText(nameA);
        price.setText(priceA);
        type.setText(typeA);

        return convertView;
    }
}