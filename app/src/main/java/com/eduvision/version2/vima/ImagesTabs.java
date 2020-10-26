package com.eduvision.version2.vima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 */
public class ImagesTabs extends Fragment {
    public ImagesTabs() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View convertView =  inflater.inflate(R.layout.shop_images_tabs, container, false);
        ListView myList = convertView.findViewById(R.id.listview);
        myList.setAdapter(new ArticleAdapter(getContext(), 1, "ShopArticles"));
        return convertView;
    }

    }

