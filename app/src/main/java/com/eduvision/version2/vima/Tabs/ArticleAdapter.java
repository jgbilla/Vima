package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.articlePage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ArticleAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater myInflater;
    Object dif;
    String myLayout;
    public static IndividualArticle myArticle;
    public static IndividualArticle myArticle_one;

    public ArticleAdapter(Context c, Object n, String forLayout) {
        mContext = c;
        dif = n;
        this.myInflater = LayoutInflater.from(c);
        myLayout = forLayout;
    }

    public int getCount() {
        return 16;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static void glideIt(ImageView image, String myArticle, Context mContext){
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(myArticle);

        Glide.with(mContext)
                .load(storageReference)
                .into(image);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            switch (myLayout) {
                case "Recents":
                    if (Fetching.isDataFetched) {
                        myArticle = Fetching.myData.get(Fetching.RecentsPageNumber * position * 2);
                        myArticle_one = Fetching.myData.get(Fetching.RecentsPageNumber * position * 2 + 1);
                        convertView = myInflater.inflate(R.layout._pop_article_model, parent, false);
                        ImageButton myBtn = convertView.findViewById(R.id.like_button);

                        TextView myShop = convertView.findViewById(R.id.shop_two);
                        TextView myDescription = convertView.findViewById(R.id.article_name_two);
                        TextView myPrice = convertView.findViewById(R.id.article_price_two);
                        myShop.setText(myArticle.getShop_name());
                        myDescription.setText(myArticle.getName());
                        myPrice.setText(myArticle.getPrice().toString());
                        ArticleAdapter.glideIt(convertView.findViewById(R.id.article_picture_two), myArticle.getP_photo(), mContext);

                        Fetching.changeText(convertView, myArticle, mContext);
                        int finalPosition = position;

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent myIntent = new Intent(mContext, articlePage.class);
                                myIntent.putExtra("LockerKey", finalPosition);
                                mContext.startActivity(myIntent);
                            }
                        });
                        myBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toggle from icon_a to icon_b or vice-versa
                                //Add item to likedItems
                                //I tried with Fetching.handleLike but it had some weird behavior
                                //Fetching.handleLike written as a comment
                            }
                        });
                    }
                    break;
                case "Popular":
                    convertView = myInflater.inflate(R.layout.recent_article_model, parent, false);
                    if (Spinning.isDataFetched) {
                        myArticle = Home.mySortedData.get(Fetching.PopularPageNumber * position);

                        Fetching.changeText(convertView, myArticle, mContext);
                        int finalPosition = position;
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(mContext, articlePage.class);
                                myIntent.putExtra("LockerKey", finalPosition);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(myIntent);
                            }
                        });
                    }
                    break;
                case "Boutiques":
                    convertView = myInflater.inflate(R.layout.shop_article_model, parent, false);
                    IndividualShop myShop = Spinning.shopData.get(position);
                    TextView shopName = convertView.findViewById(R.id.name);
                    shopName.setText(myShop.getName());
                    TextView location = convertView.findViewById(R.id.location);
                    location.setText(myShop.getLocation());
                    ImageView myImage = convertView.findViewById(R.id.article_picture);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Spinning.shopData.get(position).getP_photo());
                    Glide.with(mContext)
                            .load(storageReference)
                            .into(myImage);

                    break;
                case "ShopArticles":
                    convertView = myInflater.inflate(R.layout.article_in_shop_model, parent, false);
                    ArrayList<Long> myArticles = (ArrayList<Long>) dif;

                    IndividualArticle fArticle = Spinning.myData.get(Math.toIntExact(myArticles.get(position * 3 )));
                    IndividualArticle sArticle = Spinning.myData.get(Math.toIntExact(myArticles.get(position * 3 +1)));
                    IndividualArticle tArticle = Spinning.myData.get(Math.toIntExact(myArticles.get(position * 3 +2)));

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

                    break;
                default:
                    convertView = myInflater.inflate(R.layout.recent_article_model, parent, false);
                    break;
            }

        }

        return convertView;
    }

}