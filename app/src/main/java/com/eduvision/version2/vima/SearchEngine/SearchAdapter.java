package com.eduvision.version2.vima.SearchEngine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.ShopModel;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.articlePage;
import com.eduvision.version2.vima.shopPage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;

    ArrayList<IndividualArticle> articleList;
    ArrayList<ShopModel> shopList;
    ArrayList<String> typeList;
    ArrayList<Integer> positionList;

    public SearchAdapter(Context context, ArrayList<IndividualArticle> articleList, ArrayList<ShopModel> shopList, ArrayList<String> typeList, ArrayList<Integer> positionList){
        this.context = context;
        this.articleList = articleList;
        this.shopList = shopList;
        this.typeList = typeList;
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
        return new SearchViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Log.d(TAG, "Value is: " + typeList);
        String type = typeList.get(position);

        if (!articleList.isEmpty()) {


            for (int i = 0; i < articleList.size(); i++) {




                if (type.equals("Article")) {
                    holder.name.setText((CharSequence) articleList.get(i).getName());
                    holder.type.setText(type);

                    String base = articleList.get(i).getP_photo();

                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(base);

                    Glide.with(context)
                            .load(reference)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("TAG", "Error Loading image", e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(holder.image);

                    if (!articleList.get(i).getName().equals("Aucun article ne repond a votre recherche")) {
                        int finalI = i;

                        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String type = typeList.get(position);
                                int p = Spinning.myData.indexOf(articleList.get(finalI));

                                if (type.equals("Article")) {
                                    String currentName = articleList.get(finalI).getName();


                                    if (currentName.contains(holder.name.getText().toString())) {
                                        Intent myIntent = new Intent(context, articlePage.class);
                                        myIntent.putExtra("LockerKey", p);
                                        context.startActivity(myIntent);

                                    }
                                }


                            }
                        });
                    }
                }

            }
        }

        if (!shopList.isEmpty()) {

            for (int i = 0; i < shopList.size(); i++) {
                int index = i+articleList.size();
                Log.d(TAG, "Value is: " + index);


                if (type.equals("Boutique")) {

                    holder.name.setText((CharSequence) shopList.get(i).getName());
                    holder.type.setText(type);

                    String base = shopList.get(i).getP_photo();

                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(base);

                    Glide.with(context)
                            .load(reference)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("TAG", "Error Loading image", e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(holder.image);

                    if (!articleList.get(0).getName().equals("Aucun article ne repond a votre recherche")) {
                        int finalI = i;

                        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String type = typeList.get(finalI);
                                int p = Spinning.shopData.indexOf(shopList.get(finalI));

                                if (type.equals("Boutique")) {
                                    String currentName = shopList.get(finalI).getName();


                                    if (currentName.contains(holder.name.getText().toString())) {
                                        Intent myIntent = new Intent(context, shopPage.class);
                                        myIntent.putExtra("LockerKey", p);
                                        context.startActivity(myIntent);

                                    }
                                }


                            }
                        });

                    }


                }

                  /*if(type.equals("Shop")) {

                            String currentShop = shopList.get(position).getName();
                            if (currentShop.contains(holder.name.toString())) {
                                Intent Intent = new Intent(context, shopPage.class);
                                Intent.putExtra("LockerKey", position);
                                context.startActivity(Intent);
                            }
                        }*/


            }
            // Setting image on image view using Bitmap
        }




    }



    @Override
    public int getItemCount() {
        return articleList.size() + shopList.size();
    }

    static class  SearchViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        ImageView image;
        TextView name;
        TextView type;

        public SearchViewHolder(@NonNull  View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_result);
            name = itemView.findViewById(R.id.name_text);
            linearLayout = itemView.findViewById(R.id.result_layout);
            type = itemView.findViewById(R.id.type);
        }
    }


    }




