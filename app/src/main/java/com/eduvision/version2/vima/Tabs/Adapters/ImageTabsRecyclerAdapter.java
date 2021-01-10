package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
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

    ArrayList<Long> myArticles;

    public ImageTabsRecyclerAdapter(ArrayList<IndividualArticle> mData, Object dif, Context mContext) {
        this.myData = mData;
        this.dif = dif;
        this.mContext = mContext;
        myArticles = (ArrayList<Long>) dif;
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


        IndividualArticle fArticle = Spinning.myData.get((position * 3 ));
        IndividualArticle sArticle = Spinning.myData.get((position * 3 +1));
        IndividualArticle tArticle = Spinning.myData.get((position * 3 +2));
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
        return Spinning.myData.size()/3;
    }
}