package com.eduvision.version2.vima;

import android.content.Context;
import android.media.MediaPlayer;
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
I used this class as a BaseAdapter for both the Recent and the Popular Activities
 */

public class ArticleAdapter extends BaseAdapter {
    protected int index;
    ArrayList<individual_info_class> myList = Recents.getMyList();
    String nameA, priceA, typeA, photoA, shopA;
    Context mContext; boolean isLiked = false;
    private DatabaseReference mDatabase;
    protected ArrayList<individual_info_class> article_list;
    private FirebaseStorage myFireBaseStorage;

    public ArticleAdapter(Context context, ArrayList<individual_info_class> theList, int index) {
        this.mContext = context;
        this.index = index;
        this.article_list = theList;
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
        final individual_info_class temp = myList.get(position);
        return temp.getRank();
    }

    //******************************************************************************************************************
    //Creating the View that will be passed on
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Getting the info for current article
        final individual_info_class temp = article_list.get(position);
        nameA = temp.getName();
        priceA = temp.getPrice();
        photoA = temp.getP_photo();
        shopA = temp.getShop_name();
        myFireBaseStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (convertView == null) {
            final LayoutInflater layoutInflater= LayoutInflater.from(mContext);
            switch(index){
                /*This index will decide whether we are in the Recent or Popular cases
                You can add more cases for this index to suit your interests ;)
                Anyways the layout should contain:
                A like button and TextViews for the name, photo, price and shop
                You will provide suitable infos as an ArrayList<individual_info_class> in the constructor*/
                case 1:
                    convertView = layoutInflater.inflate(R.layout.model, null);
                case 2:
                    convertView = layoutInflater.inflate(R.layout.model2, null);
            }
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
        //Well, we set an image if the like button is pushed and another one otherwise
        //TODO: Add a node in Article where we can find the users who liked and check whether it was already liked or no
        like_button.setImageResource(R.drawable.likeA);

        //mediaPlayer is used to create a ding sound when like button is pressed
        MediaPlayer mediaPlayer= MediaPlayer.create(like_button.getContext(),R.raw.ding_sound);
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("articles").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        isLiked = !isLiked;
                        final int[] counter = new int[1];
                        String id = Integer.toString(temp.getRank());
                        counter[0] = (int) dataSnapshot.child(id).child("infos").child("likes").getValue();
                        if(isLiked){
                            like_button.setImageResource(R.drawable.likeB);
                            mediaPlayer.start();
                            mDatabase.child("articles").child("info").child("likes").setValue(counter[0]++);
                        }
                        else{
                            like_button.setImageResource(R.drawable.likeA);
                            mediaPlayer.start();
                            mDatabase.child("articles").child("info").child("likes").setValue(counter[0]--);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        return convertView;
    }
}