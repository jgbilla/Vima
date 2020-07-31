package com.eduvision.version2.vima.BoutiquesTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.R;

//An adapter to display the shops correctly

public class Adapter extends  RecyclerView.Adapter<Adapter.HorizontalViewHolder>{

    private ShopConstructor[] shops;
    private Context context;


    public Adapter(ShopConstructor[] shops, Context context) {
        this.shops = shops;
        this.context = context;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.boutique_item, parent, false);
        return new HorizontalViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        holder.Name.setText(shops[position].name);
        holder.Location.setText(shops[position].location);
        holder.linearLayout.setBackground(context.getResources().getDrawable(shops[position].logo));


    }

    @Override
    public int getItemCount() {
        return shops.length;
    }

    public  class HorizontalViewHolder extends RecyclerView.ViewHolder{
        TextView Location;
        TextView Name;
        LinearLayout linearLayout;
        ShopConstructor shop;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            Location = itemView.findViewById(R.id.location);
            Name = itemView.findViewById(R.id.name);
            linearLayout = itemView.findViewById(R.id.image_view);
        }
    }
}
