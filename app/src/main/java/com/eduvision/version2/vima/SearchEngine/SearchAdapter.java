package com.eduvision.version2.vima.SearchEngine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<String> nameList;
    ArrayList<StorageReference> photoList;


    public SearchAdapter(Context context, ArrayList<String> nameList,  ArrayList<StorageReference> photoList){
        this.context = context;
        this.nameList = nameList;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {


        holder.name.setText(nameList.get(position));
        Glide.with(context)
                .load(photoList.get(position))
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



        // Setting image on image view using Bitmap

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class  SearchViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_result);
            name = itemView.findViewById(R.id.name_text);
        }
    }


    }




