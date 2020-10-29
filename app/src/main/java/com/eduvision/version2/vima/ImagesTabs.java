package com.eduvision.version2.vima;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.ShopImagesAdapter;

import java.util.ArrayList;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 */
public class ImagesTabs extends Fragment {
    public ArrayList<Long> myArticles;
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
        // Inflate the layout for this fragment
        //In the database, we have a node called Articles
        //In articles, we have multiple subnodes for the titles (we will limit those to three)
        //We load each subnode and
        /*
        Map will map from each "name" to each ArrayList "1, 2, 3, 4, ..."
        Articles
            1
                name: Veste
                1: 1
                2: 3
                3: 4
            2
            `   name: Hauts


            In FetchShops:---------
            for(i<3)
                for(int a< snapshot.child(i).getChildrenCount())
                    ArticlesArray.add (snapshot.child(i).get(a))
                    Titles.add(snapshot.child(i).get(name))
                Map.put(i, ArticlesArray)

            In shopPage:-------------------------
            get Article Arrays and pass them to ImagesTabs
            Article1 = shop.mMap.get(1)
            Article2 = shop.mMap.get(2)
            Article3 = shop.mMap.get(3)
            addFragment(new ImagesTabs(Article1), Titles(1))
            addFragment(new ImagesTabs(Article2), Titles(2))
            addFragment(new ImagesTabs(Article3), Titles(3))

            In ImagesTabs
            Fetching.get
         */
        setHasOptionsMenu(true);

        View convertView =  inflater.inflate(R.layout.shop_images_tabs, container, false);
        ListView myList = convertView.findViewById(R.id.listview);
        myList.setAdapter(new ArticleAdapter(getContext(), myArticles, "ShopArticles"));
        return convertView;
    }

}

