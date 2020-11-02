package com.eduvision.version2.vima.BoutiquesTab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShopAdapter extends BaseAdapter {

    private final Context mContext;
    private final ShopConstructor[] shops;

    public ShopAdapter(Context context, ShopConstructor[] shops){
        this.mContext = context;
        this.shops = shops;
    }
    @Override
    public int getCount(){
        return  shops.length;
}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setText(String.valueOf(position));
        return dummyTextView;
    }
}
