package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.ImagesTabs;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.articlePage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ImageTabsRecyclerAdapter extends RecyclerView.Adapter<ImageTabsRecyclerAdapter.MyViewHolder> {
    private ArrayList<IndividualArticle> myData;
    private Context mContext;
    Object dif;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View myLayout;
        public ImageView myImage;
        public MyViewHolder(View v) {
            super(v);
            myLayout = v;
            //myImage.findViewById(R.id.article_picture);
        }
        public View getView(){
            return myLayout;
        }
    }

    public ImageTabsRecyclerAdapter(ArrayList<IndividualArticle> mData,  Context mContext) {
        this.myData = mData;
        this.mContext = mContext;
    }

    @Override
    public ImageTabsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_in_shop_model, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        View convertView =  holder.myLayout;
        CardView first = convertView.findViewById(R.id.first_image);
        CardView second = convertView.findViewById(R.id.second_image);
        CardView third = convertView.findViewById(R.id.third_image);

        IndividualArticle fArticle = new IndividualArticle();
        IndividualArticle sArticle = new IndividualArticle();
        IndividualArticle tArticle = new IndividualArticle();
        Log.println(Log.DEBUG, "POSITION", String.valueOf(position));

        Log.println(Log.DEBUG, "SIZE", String.valueOf(myData.size()));
        Log.println(Log.DEBUG, "SIZE MODULO", String.valueOf(myData.size()%3));
        if(position*3 >= myData.size() & myData.size()>=3) {
            if (myData.size() % 3 == 1) {
                fArticle = myData.get((position * 3));
                sArticle = myData.get(1);
                tArticle = myData.get(2);
            } else if (myData.size() % 3 == 2 & position > myData.size()) {
                fArticle = myData.get((position * 3));
                sArticle = myData.get((position * 3 + 1));
                tArticle = myData.get(1);
            } else if (myData.size() % 3 == 0) {
                fArticle = myData.get((position * 3));
                sArticle = myData.get((position * 3 + 1));
                tArticle = myData.get((position * 3 + 2));
            }
        }
        else if(myData.size()<3){
            fArticle = myData.get(0);
            sArticle = myData.get(1);
            tArticle = myData.get(0);
        }
        else{
            fArticle = myData.get((position * 3));
            sArticle = myData.get((position * 3 + 1));
            tArticle = myData.get((position * 3 + 2));
        }
        Log.println(Log.DEBUG, "P_PHOTO", String.valueOf(fArticle.getP_photo()));
        Log.println(Log.DEBUG, "P_PHOTO", String.valueOf(sArticle.getP_photo()));
        Log.println(Log.DEBUG, "P_PHOTO", String.valueOf(tArticle.getP_photo()));

        ImageButton myBtn1 = first.findViewById(R.id.like_button1);
        ImageButton myBtn2 = second.findViewById(R.id.like_button2);
        ImageButton myBtn3 = third.findViewById(R.id.like_button3);

        if(fArticle.isLiked) {
            myBtn1.setImageResource(R.drawable.icon_b);
        }
        else{
            myBtn1.setImageResource(R.drawable.icon_a);
        }
        IndividualArticle finalFArticle = fArticle;
        myBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.handleLike(myBtn1, finalFArticle, mContext);
                for(IndividualArticle likedArticle : myData){
                    if (likedArticle.positionInDataBase == finalFArticle.positionInDataBase) {
                        likedArticle.isLiked = true;
                        notifyDataSetChanged();
                        ImagesTabs.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if(sArticle.isLiked) {
            myBtn2.setImageResource(R.drawable.icon_b);
        }
        else{
            myBtn2.setImageResource(R.drawable.icon_a);
        }
        IndividualArticle finalSArticle = sArticle;
        myBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.handleLike(myBtn2, finalSArticle, mContext);
                for(IndividualArticle likedArticle : myData){
                    if (likedArticle.positionInDataBase == finalSArticle.positionInDataBase) {
                        likedArticle.isLiked = true;
                        notifyDataSetChanged();
                        ImagesTabs.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if(tArticle.isLiked) {
            myBtn3.setImageResource(R.drawable.icon_b);
        }
        else{
            myBtn3.setImageResource(R.drawable.icon_a);
        }
        IndividualArticle finalTArticle = tArticle;
        myBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.handleLike(myBtn3, finalTArticle, mContext);
                for(IndividualArticle likedArticle : myData){
                    if (likedArticle.positionInDataBase == finalTArticle.positionInDataBase) {
                        likedArticle.isLiked = true;
                        notifyDataSetChanged();
                        ImagesTabs.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, articlePage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                myIntent.putExtra("LockerKey", position * 3 );
                mContext.startActivity(myIntent);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, articlePage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                myIntent.putExtra("LockerKey", position * 3  + 1);
                mContext.startActivity(myIntent);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, articlePage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                myIntent.putExtra("LockerKey", position * 3  + 2);
                mContext.startActivity(myIntent);
            }
        });


        TextView price1 = convertView.findViewById(R.id.article_price1);
        price1.setText(fArticle.getName());
        TextView description1 = convertView.findViewById(R.id.article_description1);
        description1.setText(fArticle.getName());
        ImageView myImage1 = convertView.findViewById(R.id.article_picture1);
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(fArticle.getP_photo());
        Glide.with(mContext)
                .load(storageReference1)
                .into(myImage1);

        TextView price2 = convertView.findViewById(R.id.article_price2);
        price2.setText(sArticle.getName());
        TextView description2 = convertView.findViewById(R.id.article_description2);
        description2.setText(sArticle.getName());
        ImageView myImage2 = convertView.findViewById(R.id.article_picture2);
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(sArticle.getP_photo());
        Glide.with(mContext)
                .load(storageReference2)
                .into(myImage2);

        TextView price3 = convertView.findViewById(R.id.article_price3);
        price3.setText(tArticle.getName());
        TextView description3 = convertView.findViewById(R.id.article_description3);
        description3.setText(tArticle.getName());
        TextView name = convertView.findViewById(R.id.article_name3);
        name.setText(tArticle.getName());
        ImageView myImage3 = convertView.findViewById(R.id.article_picture3);
        StorageReference storageReference3 = FirebaseStorage.getInstance().getReferenceFromUrl(tArticle.getP_photo());
        Glide.with(mContext)
                .load(storageReference3)
                .into(myImage3);

    }

    @Override
    public int getItemCount() {
        return (myData.size()+myData.size()%3)/3;
    }


}