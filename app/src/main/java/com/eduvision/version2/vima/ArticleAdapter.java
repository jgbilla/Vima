package com.eduvision.version2.vima;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.facebook.FacebookSdk.getApplicationContext;
/*
I used this class as a BaseAdapter for the "recent" Activity
 */

public class ArticleAdapter extends BaseAdapter {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /*
    Global variables:
    lastId: id of the most recent article given by orangmoney activity
    nameA, priceA and typeA: the title, price and short description of the displayed article
     */
 int lastId;
    String nameA, priceA, typeA, photoA;
    Context mContext;
    private DatabaseReference mDatabase;
    StorageReference storageReference;
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
        return lastId - position ;
    }


    //******************************************************************************************************************
    //Creating the View that will be passed on
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.model, null);
        }

        myFireBaseStorage = FirebaseStorage.getInstance();

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(String photoUrl) {
                storageReference = myFireBaseStorage.getReferenceFromUrl(photoUrl);
            }
        });




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

    private void readData(final FirebaseCallback firebaseCallback){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences =getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lastId = sharedPreferences.getInt("lastId", 2);


        //Get the article name
        mDatabase.child("Articles").child(Integer.toString(lastId)).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameA = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Get the article price
        mDatabase.child("Articles").child(Long.toString(lastId)).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                priceA = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Get the article type
        mDatabase.child("Articles").child(Integer.toString(lastId)).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                typeA = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Get the article photo location
        mDatabase.child("Articles").child(Integer.toString(lastId)).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                photoA = String.valueOf(dataSnapshot.getValue());
                firebaseCallback.onCallback(photoA);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private interface FirebaseCallback {

        void onCallback(String photoUrl);

    }

}