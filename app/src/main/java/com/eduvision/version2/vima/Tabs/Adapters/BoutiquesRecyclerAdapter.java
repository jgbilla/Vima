package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.ShopModel;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.articlePage;
import com.eduvision.version2.vima.shopPage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BoutiquesRecyclerAdapter extends RecyclerView.Adapter<BoutiquesRecyclerAdapter.MyViewHolder> {
    private ArrayList<IndividualArticle> myData;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout myLayout;
        public MyViewHolder(LinearLayout v) {
            super(v);
            myLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BoutiquesRecyclerAdapter(ArrayList<IndividualArticle> mData, Context mContext) {
        this.myData = mData;
        this.mContext = mContext;
    }

    @Override
    public BoutiquesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_article_model, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        View convertView = holder.myLayout;
        ShopModel myShop = Spinning.shopData.get(position);
        TextView shopName = convertView.findViewById(R.id.name);
        shopName.setText(myShop.getName());
        TextView location = convertView.findViewById(R.id.location);
        location.setText(myShop.getLocation());
        ImageView myImage = convertView.findViewById(R.id.article_picture);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Spinning.shopData.get(position).getP_photo());
        Glide.with(mContext)
                .load(storageReference)
                .into(myImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, shopPage.class);
                myIntent.putExtra("LockerKey", position);
                mContext.startActivity(myIntent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return Spinning.shopData.size();
    }
}