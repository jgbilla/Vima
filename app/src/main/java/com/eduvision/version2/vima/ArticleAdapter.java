package com.eduvision.version2.vima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
/*
I used this class as a BaseAdapter for the "recent" Activity
 */

public class ArticleAdapter extends BaseAdapter {
    /*
    Global variables:
    lastId: id of the most recent article given by orangemoney activity
    nameA, priceA and typeA: the title, price and short description of the displayed article
     */

    final int lastId = 0;
    String nameA, priceA, typeA, photoA, shopA;
    Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseStorage myFireBaseStorage;

    public ArticleAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override

    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //******************************************************************************************************************
    //Creating the View that will be passed on
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.model, null);
        }
        ArrayList<ArrayList<String>> myList = new ArrayList<>(0);
        myList = OrangeMoney.getMyList();
        ArrayList<String> temp = myList.get(position);
        nameA = temp.get(1);
        priceA = temp.get(2);
        photoA = temp.get(3);
        shopA = temp.get(4);
        typeA = temp.get(5);

        final TextView name = convertView.findViewById(R.id.name);
        final ImageView photo = convertView.findViewById(R.id.photo);
        final TextView price = convertView.findViewById(R.id.price);
        final TextView type = convertView.findViewById(R.id.type);

        myFireBaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = myFireBaseStorage.getReferenceFromUrl(photoA);
        Glide.with(mContext)
                .load(storageReference)
                .into(photo);

        name.setText(nameA);
        price.setText(priceA);
        type.setText(typeA);

        return convertView;
    }
}