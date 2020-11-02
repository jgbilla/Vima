package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<IndividualArticle> myData;
    private Context mContext;

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

    public RecyclerAdapter(ArrayList<IndividualArticle> mData, Context mContext) {
        this.myData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liked_article_model, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(Recents.myLikedItems.size() != 0){
            View convertView =  holder.myLayout;
            TextView myShop = convertView.findViewById(R.id.shop);
            TextView myDescription = convertView.findViewById(R.id.article_name);
            TextView myPrice = convertView.findViewById(R.id.article_price);
            myShop.setText(Recents.myLikedItems.get(position).getShop_name());
            myDescription.setText(Recents.myLikedItems.get(position).getName());
            myPrice.setText(String.valueOf(Recents.myLikedItems.get(position).getPrice()));
            ArticleAdapter.glideIt(convertView.findViewById(R.id.article_picture), Recents.myLikedItems.get(position).getP_photo(), mContext);
        }
    }
    @Override
    public int getItemCount() {
        return myData.size();
    }
}