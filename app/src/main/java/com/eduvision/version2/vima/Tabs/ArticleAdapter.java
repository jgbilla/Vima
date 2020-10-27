package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ArticleAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater myInflater;
    int dif;
    FirebaseStorage storage;
    String myLayout;
    public static IndividualArticle myArticle;
    public static IndividualArticle myArticle_one;
    ArrayList<IndividualArticle> MyList;

    public ArticleAdapter(Context c, int n, String forLayout) {
        mContext = c;
        dif = n;
        this.myInflater = LayoutInflater.from(c);
        myLayout = forLayout;
        /*ArrayList<individual_info_class> givenList,
        MyList = givenList;*/
    }

    public int getCount() {
       /* int count;
        if(myLayout == "Popular"){
            count = Fetching.PopularPageNumber;
        }
        else if(myLayout == "Recents"){
            count = Fetching.RecentsPageNumber;
        }
        else{
            count = 16;
        }

        */
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

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            switch (myLayout){
                case "Recents":
                    if(Fetching.isDataFetched.equals("Yes")) {
                        myArticle = Fetching.myData.get(Fetching.RecentsPageNumber*position*2);
                        myArticle_one = Fetching.myData.get(Fetching.RecentsPageNumber*position*2+1);
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
                    if(Fetching.isDataFetched.equals("Yes")){
                        myArticle = Home.mySortedData.get(Fetching.PopularPageNumber*position);

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
                    }
                    break;
                case "Boutiques":
                    convertView = myInflater.inflate(R.layout.shop_article_model, parent, false);
                    IndividualShop myShop = FetchShops.shopData.get(position);
                    TextView shopName = convertView.findViewById(R.id.name);
                    shopName.setText(myShop.getName());
                    TextView location = convertView.findViewById(R.id.location);
                    location.setText(myShop.getLocation());
                    ImageView myImage = convertView.findViewById(R.id.article_picture);
                    Log.println(Log.INFO,"Tagging",  FetchShops.shopData.get(position).getP_photo());
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(FetchShops.shopData.get(position).getP_photo());
                    Glide.with(mContext)
                            .load(storageReference)
                            .into(myImage);

                    break;
                case "ShopArticles":
                    convertView = myInflater.inflate(R.layout.article_in_shop_model, parent, false);

                    /*
                    myImage = convertView.findViewById(R.id.article_shop_picture);
                    ImageView myImage1 = convertView.findViewById(R.id.second_article_shop_picture);
                    ImageView myImage2 = convertView.findViewById(R.id.shop_picture);
                    glideIt(myImage, Fetching.getItems(position*3, "Articles"));
                    glideIt(myImage1, Fetching.getItems(position*3-1, "Articles"));
                    glideIt(myImage2, Fetching.getItems(position*3-2, "Articles"));
                     */
                    break;
                default:
                    convertView = myInflater.inflate(R.layout.recent_article_model, parent, false);
                    break;
            }

            /*
            ImageView myView = (ImageView) convertView.findViewById(R.id.shop_picture);
            TextView type = (TextView) convertView.findViewById(R.id.article_description);
            TextView price = (TextView) convertView.findViewById(R.id.article_price);
            TextView name = (TextView) convertView.findViewById(R.id.article_name);

            Glide.with(mContext)
                    .load(MyList.get(position).getP_photo())
                    .into(myView);
            type.setText(MyList.get(position).getShop_name());
            name.setText(MyList.get(position).getName());
            price.setText(MyList.get(position).getPrice());
             */

        }

        return convertView;
    }

}