package com.eduvision.version2.vima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Profile_Article_Adapter extends BaseAdapter {
    Context mContext;
    TextView added, type, period;
    ImageView photo;
    static ArrayList<String[]> profiles;

    public Profile_Article_Adapter(ArrayList<String[]> profiles) {
        Profile_Article_Adapter.profiles = profiles;
    }

    public Profile_Article_Adapter(){

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] infos;
        infos = Profile_Article_Adapter.profiles.get(position);
        String articleType = infos[0];
        String timePeriod = infos[1];
        String textAdded = infos[2];
        String photoA = infos[3];
        if (convertView == null) {
            final LayoutInflater layoutInflater= LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.model3, null);
        }
        added = convertView.findViewById(R.id.added);
        type = convertView.findViewById(R.id.type);
        period = convertView.findViewById(R.id.period);

        added.setText(textAdded);
        type.setText(articleType);
        period.setText(timePeriod);

        Glide.with(mContext)
                .load(photoA)
                .into(photo);

        return convertView;
    }
}
