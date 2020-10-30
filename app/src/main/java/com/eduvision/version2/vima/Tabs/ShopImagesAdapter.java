package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.Home;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.articlePage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
/*
I used this class as a BaseAdapter for both the Recent and the Popular Activities
 */

public class ShopImagesAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater myInflater;
    int dif;
    FirebaseStorage storage;
    String myLayout;
    public static IndividualArticle myArticle;
    public static IndividualArticle myArticle_one;
    ArrayList<IndividualArticle> MyList;

    ArrayList<Long> myArticles;
    public ShopImagesAdapter(Context c, ArrayList<Long> givenArticles) {
        mContext = c;
        this.myInflater = LayoutInflater.from(c);
        this.myArticles = givenArticles;
    }
    public int getCount() {
            return Math.round(myArticles.size()/3) -2;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            IndividualArticle fArticle = Fetching.myData.get(Math.toIntExact(myArticles.get(position * 3)));
            IndividualArticle sArticle = Fetching.myData.get(Math.toIntExact(myArticles.get(position * 3+1)));
            IndividualArticle tArticle = Fetching.myData.get(Math.toIntExact(myArticles.get(position * 3+2)));

            convertView = myInflater.inflate(R.layout.article_in_shop_model, parent, false);
            /*
            View view1 = convertView.findViewById(R.id.view1);
            View view2 = convertView.findViewById(R.id.view1);
            View view3 = convertView.findViewById(R.id.view1);

            Fetching.changeText(view1, fArticle, mContext);
            Fetching.changeText(view2, sArticle, mContext);
            Fetching.changeText(view3, tArticle, mContext);

             */
            /*
            myImage = convertView.findViewById(R.id.article_shop_picture);
            ImageView myImage1 = convertView.findViewById(R.id.second_article_shop_picture);
            ImageView myImage2 = convertView.findViewById(R.id.shop_picture);
            glideIt(myImage, Fetching.getItems(position*3, "Articles"));
            glideIt(myImage1, Fetching.getItems(position*3-1, "Articles"));
            glideIt(myImage2, Fetching.getItems(position*3-2, "Articles"));
             */
            }


        return convertView;
    }

}