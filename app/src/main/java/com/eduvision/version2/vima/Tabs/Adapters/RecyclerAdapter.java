package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.Favorite;
import com.eduvision.version2.vima.Home;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.eduvision.version2.vima.articlePage;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(mContext, articlePage.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    myIntent.putExtra("LockerKey", Recents.myLikedItems.get(position).positionInArray);
                    mContext.startActivity(myIntent);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Effacer cet Article")
                            .setMessage("Voulez-vous effacer ce article de votre historique? Cette action est irréversible.")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(IndividualArticle Article : Spinning.myData){
                                        if(Recents.myLikedItems.get(position).positionInDataBase == Article.positionInDataBase){
                                            Article.isLiked = false;
                                        }
                                    }

                                    if (Recents.myLikedItems.size()==0){

                                    }
                                    else{
                                        SharedPreferences prefs = mContext.getSharedPreferences("prefs",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        Gson gson = new Gson();
                                        String jsonText = gson.toJson(Recents.myLikedItems);
                                        editor.putString("LikedItems", jsonText);
                                        editor.apply();
                                    }
                                }
                            })
                            .setNegativeButton("Non", null)
                            .show();
                    return false;
                }
            });
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