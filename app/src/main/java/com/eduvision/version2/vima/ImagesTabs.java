package com.eduvision.version2.vima;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.Tabs.Adapters.ImageTabsRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Adapters.RecentsRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 */
public class ImagesTabs extends Fragment {
    public ArrayList<Long> myArticles;
    static RecyclerView articlegv;
    public ImagesTabs(ArrayList<Long> myArticles) {
        this.myArticles = myArticles;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View convertView =  inflater.inflate(R.layout.shop_images_tabs, container, false);

        ImageTabsRecyclerAdapter mAdapter = new ImageTabsRecyclerAdapter(Spinning.myData, myArticles,  getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        articlegv = convertView.findViewById(R.id.listview);
        articlegv.setHasFixedSize(true);
        articlegv.setLayoutManager(manager);
        articlegv.setAdapter(mAdapter);

        return convertView;

        /*
        Slideshow and Contact Icons

        Statistics Collecting
            how many people logged in on a day


        Icon for Vima Image
         */
    }

}

