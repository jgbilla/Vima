package com.eduvision.version2.vima;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Comparator;

public class Popular extends Fragment {
    public static Popular newInstance(int page, String title) {
        //
        Popular populars = new Popular();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        populars.setArguments(args);
        return populars;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_tab2, container, false);

        super.onCreate(savedInstanceState);
        GridView articlegv = view.findViewById(R.id.gridview_popular);
        Recents.getItems(100);
        ArrayList<individual_info_class> pop_list = Recents.getMyList();
        //We are ranking the articles by order of popularity
        pop_list.sort(Comparator.comparingInt(individual_info_class::getPopularity_index).reversed());

        articlegv.setAdapter(new ArticleAdapter(getActivity(), pop_list));
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*Get to specific chosen Article page,It is not complete yet tho*/
                Intent myIntent = new Intent(getActivity(), articlePage.class);
                myIntent.putExtra("id", (Parcelable) pop_list.get(position));
                startActivity(myIntent);
            }
        });

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
